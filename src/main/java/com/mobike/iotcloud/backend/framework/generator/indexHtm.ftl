<${r'@admui.app'}>
<${r'@admui.pageIndexNavbar'}>
	<${r'@admui.consoleFuncButton'} funcLevel="1" ><${r'/@admui.consoleFuncButton'}>
<${r'/@admui.pageIndexNavbar'}>

<${r'#assign'} ecc=JspTaglibs["/WEB-INF/tld/extremecomponents.tld"] />

<${r'@ecc.table'} theme="table-responsive" 
	styleClass="table table-condensed table-hover table-bordered" 
	action="index" method="get" items="list" 
	var="var" view="ecan" width="100%"
	imagePath="/platform/images/table/*.gif" showExports="false">
	<${r'@ecc.row'}>
		<${r'@ecc.column'} title="&nbsp;" property="&nbsp;" width="30" filterable="false" sortable="false">
			<input type="checkbox" data-id="${r'${var.id}'}"/>
		</${r'@ecc.column'}>
		<#list table.columns as x>
			<#if x.pageType=="img">
				<${r'@ecc.column'} title="${x.code?cap_first}" property="${x.code}" width="30">
					<img src="${r'${var.'}${x.code}${r'}'}" style="width:50px;height:50px;"/>
				</${r'@ecc.column'}>
			<#elseif x.pageType=="file">
				<${r'@ecc.column'} title="${x.code?cap_first}" property="${x.code}" width="30">
					<${r'#if'} var.${x.code}?? && var.${x.code}!="">
						<a href="${'${'}var.${x.code}${'}'}" target="_blank">download</a>
					</${r'#if'}>
				</${r'@ecc.column'}>
			<#elseif x.pageType=="textArea">
			<#else>
				<#if x.code=="gender">
					<${r'@ecc.column'} title="${x.code?cap_first}" property="${x.code}" width="30" filterCell="droplist" filterOptions="DOMAIN_GENDER" >
						<label class="label label-default">${r'${DOMAINS.GENDER[var.gender?string]}'}</label>
					</${r'@ecc.column'}>
				<#else>
					<${r'@ecc.column'} title="${x.code?cap_first}" property="${x.code}" width="30"></${r'@ecc.column'}>
				</#if>
			</#if>
			
		</#list>
		
		<${r'@ecc.column'} title="Operation" property="operation" width="200" filterable="false" sortable="false">
			<${r'@admui.consoleFuncButton'} funcLevel="2" dataId="${r'${var.id}'}" ><${r'/@admui.consoleFuncButton'}>
		<${r'/@ecc.column'}>
		
	<${r'/@ecc.row'}>
<${r'/@ecc.table'}>

<${r'#assign'} footerScripts>
<script type="text/javascript">

</script>
</${r'#assign'}>
${r'${Holder.pushScript(footerScripts)}'}
</${r'@admui.app'}>