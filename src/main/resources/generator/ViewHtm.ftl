<${r'@admui.app'}>

<${r'@admui.pageViewNavbar'} id=dto.id title="${className}" save="">
	<${r'@admui.consoleFuncButton'} funcLevel="3" dataId="${r'${dto.id}'}" ><${r'/@admui.consoleFuncButton'}>
</${r'@admui.pageViewNavbar'}>

<form id="dataForm" action="create" method="post" role="form"  class="alert alert-notice">
	<input type="hidden" id="tid" name="id" value="${r'${dto.id}'}" />
	<${r'#if'} dto.createTime??>
	<input type="hidden" name="createTime" value="${r'${'}dto.createTime?string('yyyy-MM-dd HH:mm:ss')${r'}'}" />
	</${r'#if'}>
	<div class="row">		
		<div class="col-md-8">
			<#list table.columns as x>
				<#if x.code != "id" && x.code!="createTime">
					<#if x.pageType=="img">

						<div class="form-group">
							<label>${x.code?cap_first}<small class="text-primary">${x.comments}</small></label>
							<${r'@admui.dropzone'} id="${x.code}_id" iptName="${x.code}" maxFiles=3  values="${r'${'}dto.${x.code}${r'}'}" thumbWidth=150 thumbHeight=150 acceptedFiles=".jpg" />
						</div>

					<#elseif x.pageType=="file">

						<div class="form-group">
							<label>${x.code?cap_first}<small class="text-primary">${x.comments}</small></label>
							<${r'@admui.dropzoneattach'} id="${x.code}_id" iptName="${x.code}" maxFiles=1 maxFileSize=50  values="${r'${'}dto.${x.code}${r'}'}" boxWidth=100 boxHeight=20 acceptedFiles=".pdf,.doc,.docx"/>
						</div>

					<#elseif x.pageType=="textArea">

						<div class="form-group">
				            <label>${x.code?cap_first} <small class="text-primary">${x.comments}</small></label>
				           	<${r'@admui.editor'} name="${x.code}" num="" val="${r'${'}dto.${x.code}${'}'}" height=300></${r'@admui.editor'}>
				        </div>

				    <#elseif x.pageType=="select">

				    	<div class="form-group">
				            <label>${x.code?cap_first} <small class="text-primary">${x.comments}</small></label>
				            <select name="${x.code}"  class="form-control">
				              <option value="1" <${r'#if'} dto.${x.code}=="1">selected="selected"</${r'#if'}>>YES</option>
				              <option value="0" <${r'#if'} dto.${x.code}=="0">selected="selected"</${r'#if'}>>NO</option>
				            </select>
				        </div>

				    <#else>

				    	<div class="form-group">
				            <label>${x.code?cap_first}<small class="text-primary">${x.comments}</small></label>
				            <input name="${x.code}" value="${r'${'}dto.${x.code}${r'}'}" type="text" class="form-control validate[required,maxSize[60]]" />
				        </div>	

					</#if>
				</#if>
				
			</#list>
	        
		</div>
		<div class="col-md-4">
		</div>
	</div>
</form>
<${r'#assign'} footerScripts>
<script type="text/javascript">
$(document).ready(function(){
	gAjaxJsonForm("#dataForm", function(d){
		if(d.code == 0)
		{
			alert("successfully!");
			location.href="view?id=" + d.data;
		}else
		{
			alert(d.data);
		}
	});
	
});
</script>
</${r'#assign'}>
${r'${Holder.pushScript(footerScripts)}'} 
</${r'@admui.app'}>
