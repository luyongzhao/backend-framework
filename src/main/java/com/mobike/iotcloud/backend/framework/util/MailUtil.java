package com.mobike.iotcloud.backend.framework.util;

import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Properties;

/**
 * 
 * <ul>
 * <li><b>目的:</b> <br />
 * <p>
 * 邮件发送公共类
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li><b>使用示例</b><br />
 * 
 * <pre>
 * 1、使用Spring配置发送服务器信息，发送邮件：使用spring配置smtp服务器，用户名，密码信息
 * 2、new MailUtil(&quot;smtp.ecannetwork.com&quot;, &quot;username&quot;, &quot;password&quot;)
 *  .connect().sendEmail(&quot;liulibin@ecannetwork.com&quot;,
 *  &quot;liulibin@ecannetwork.com&quot;, &quot;标题&quot;, &quot;内容&quot;).disConnect();
 * </pre>
 * 
 * </li>
 * <li><b>调用例子：
 * 
 * <pre>
 * try
 * {
 * 	new MailUtil(&quot;smtp.ecannetwork.com&quot;, &quot;username&quot;, &quot;password&quot;).connect()
 * 			.sendEmail(&quot;liulibin@ecannetwork.com&quot;, &quot;liulibin@ecannetwork.com&quot;, &quot;标题&quot;, &quot;内容&quot;).disConnect();
 * 
 * 	log.info(&quot;send success!&quot;);
 * } catch (MessagingException e)
 * {
 * 	e.printStackTrace();
 * }
 * </pre>
 * 
 * </b></li>
 * 
 * </ul>
 */
public class MailUtil
{
	private static Log log = LogFactory.getLog(MailUtil.class);

	// smtp服务器
	private String smtp;

	// smtp服务器用户名
	private String user;

	// 发送方密码
	private String password;

	private Transport trans;

	private Session session;

	private static MailUtil mailUtil = null;

	/**
	 * 构造函数
	 */
	private MailUtil()
	{

	}

	private MailUtil(String smtp, String user, String password)
	{
		this.smtp = smtp;
		this.user = user;
		this.password = password;
		log.info("smtp:" + this.smtp + "\tuser:" + this.user + "\tpasswd:" + this.password);
	}

	public synchronized static MailUtil getInstance(String smtp, String user, String password,boolean withSSL)throws MessagingException,GeneralSecurityException{

	    if (mailUtil == null) {

	        mailUtil = new MailUtil(smtp,user,password);

        }

        if (mailUtil.getTrans()==null || !mailUtil.getTrans().isConnected()) {

	        if (withSSL) {
                mailUtil.connectWithSSL();
            }else{
                mailUtil.connect();
            }

            log.info("success to connect to "+user);

        }

        return mailUtil;

    }




	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：连接SMTP服务器</li>
	 * <li>适用的前提条件:</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无</li>
	 * <li>已知问题：</li>
	 * <li>调用的例子：</li>
	 * </ul>
	 * </p>
	 * 
	 * @return
	 * @throws MessagingException
	 */
	public MailUtil connect() throws MessagingException
	{

		if (session == null)
		{
			Properties props = new Properties();
			props.put("mail.smtp.host", smtp);
			props.put("mail.smtp.auth", "true");
			// Get session
			session = Session.getDefaultInstance(props, null);
			// watch the mail commands go by to the mail server
			session.setDebug(false);
		}

		if (trans == null)
		{
			trans = session.getTransport("smtp");
		}

		if (!trans.isConnected())
		{
			trans.connect(smtp, user, password);
		}

		return this;
	}

	public MailUtil connectWithSSL() throws MessagingException,GeneralSecurityException
	{

		if (session == null)
		{
			Properties props = new Properties();
			props.put("mail.smtp.host", smtp);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.port","550");
			props.put("mail.smtp.socketFactory.fallback", "true");

			MailSSLSocketFactory sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
			props.put("mail.smtp.ssl.socketFactory", sf);
			// Get session
			session = Session.getDefaultInstance(props, null);
			// watch the mail commands go by to the mail server
			session.setDebug(false);
		}

		if (trans == null)
		{
			trans = session.getTransport("smtp");
		}

		if (!trans.isConnected())
		{
			trans.connect(smtp, user, password);
		}

		return this;
	}

	public void disConnect()
	{
		if (trans != null && trans.isConnected())
		{
			try
			{
				trans.close();
			} catch (MessagingException e)
			{
				// Do nothing.
			}
		}
	}

	/**
	 * 
	 * @param from
	 *            发送人email
	 * @param to
	 *            接收人email
	 * @param subject
	 *            主题
	 * @param text
	 *            内容
	 * @throws MessagingException
	 */
	public MailUtil sendEmail(String from, String to, String subject, String text, String encoding)
			throws MessagingException
	{
		String[] filenames = new String[0];
		return sendEmail(from, to, subject, text, filenames, encoding);
	}

	public MailUtil sendEmail(String to, String subject, String text, String encoding) throws MessagingException
	{
		String[] filenames = new String[0];
		return sendEmail(user, to, subject, text, filenames, encoding);
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：发送邮件</li>
	 * <li>适用的前提条件:已调用connect</li>
	 * <li>后置条件：发送完成后请关闭连接</li>
	 * <li>例外处理：无</li>
	 * <li>已知问题：</li>
	 * <li>调用的例子：</li>
	 * </ul>
	 * </p>
	 * 
	 * @param from
	 *            发送人email
	 * @param to
	 *            接收人email
	 * @param subject
	 *            主题
	 * @param text
	 *            内容
	 * @param filename
	 *            附件
	 * @throws MessagingException
	 */
	public MailUtil sendEmail(String from, String to, String subject, String text, String filename, String encoding)
			throws MessagingException
	{
		String[] filenames = new String[1];
		filenames[0] = filename;
		return sendEmail(from, to, subject, text, filenames, encoding);
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：发送邮件</li>
	 * <li>适用的前提条件:已调用connect</li>
	 * <li>后置条件：发送完成后请关闭连接</li>
	 * <li>例外处理：无</li>
	 * <li>已知问题：</li>
	 * <li>调用的例子：</li>
	 * </ul>
	 * </p>
	 * 
	 * @param from
	 *            发送人email
	 * @param to
	 *            接收人email 多个收件人通过逗号分隔
	 * @param subject
	 *            主题
	 * @param text
	 *            内容
	 * @param filenames
	 *            附件
	 * @throws MessagingException
	 */
	public MailUtil sendEmail(String from, String to, String subject, String text, String[] filenames, String encoding)
			throws MessagingException
	{
		try
		{

			// Define message
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			String[] tos = to.split(",");
			InternetAddress[] toAddress = new InternetAddress[tos.length];
			for (int i = 0; i < tos.length; i++)
			{
				toAddress[i] = new InternetAddress(tos[i]);
			}

			msg.setRecipients(Message.RecipientType.TO, toAddress);
			msg.setHeader("X-Mailer", "Cashing send mailer Ver 1.0");
			msg.setSubject(subject);

			// 创建 Mimemultipart，这是包含多个附件是必须创建的。
			MimeMultipart multi = new MimeMultipart();

			// 创建
			// BodyPart，主要作用是将以后创建的n个内容加入MimeMultipart;第一个BodyPart.主要写一些一般的信件内容。
			BodyPart textBodyPart = new MimeBodyPart();

			// 发送内容：可以HTML
			textBodyPart.setContent(text, "text/html;charset=" + encoding);

			// 压入第一个BodyPart到MimeMultipart对象中。
			multi.addBodyPart(textBodyPart);

			if (filenames != null && filenames.length > 0)
			{
				// 添加文件
				for (int i = 0; i < filenames.length; i++)
				{
					if (filenames[i] == null || "".equals(filenames[i]))
					{
						continue;
					}
					// 创建BodyPart,是一个FileDAtaSource
					FileDataSource fds = new FileDataSource(filenames[i]);
					BodyPart fileBodyPart = new MimeBodyPart();
					// 字符流形式装入文件
					fileBodyPart.setDataHandler(new DataHandler(fds));

					// 设置文件名，可以不是原来的文件名。
					try
					{
						fileBodyPart.setFileName(MimeUtility.encodeWord(fds.getName(), encoding, null));
					} catch (Exception e)
					{
						e.printStackTrace();
					}

					multi.addBodyPart(fileBodyPart);
				}
			}

			msg.setContent(multi);

			msg.setSentDate(Calendar.getInstance().getTime());

			// Send email
			msg.saveChanges();
			trans.sendMessage(msg, msg.getAllRecipients());

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * @return the smtp
	 */
	public String getSmtp()
	{
		return smtp;
	}

	/**
	 * @param smtp
	 *            the smtp to set
	 */
	public void setSmtp(String smtp)
	{
		this.smtp = smtp;
	}

	/**
	 * @return the user
	 */
	public String getUser()
	{
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user)
	{
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * @return the trans
	 */
	public Transport getTrans()
	{
		return trans;
	}

	/**
	 * @param trans
	 *            the trans to set
	 */
	public void setTrans(Transport trans)
	{
		this.trans = trans;
	}

	public static void main(String args[])
	{
		try
		{
			MailUtil util = new MailUtil("smtp.partner.outlook.cn", "shareoffice@mobike.com", "xxx")
                    .connectWithSSL();
            log.info("connect success!");
			util.sendEmail("luyongzhao@mobike.com", "测试邮件", "Hi,luyongzhao<br/> 有一个孩子(技术组测试3)注册成为了用户，家长手机号码为17710220684,请尽快联系！", "gbk");
            log.info("send success1!");
            util.sendEmail("luyongzhao@mobike.com", "测试邮件", "Hi,luyongzhao<br/> 有一个孩子(技术组测试3)注册成为了用户，家长手机号码为17710220684,请尽快联系！", "gbk");
            log.info("send success2!");
            util.disConnect();

			log.info("send success!");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
