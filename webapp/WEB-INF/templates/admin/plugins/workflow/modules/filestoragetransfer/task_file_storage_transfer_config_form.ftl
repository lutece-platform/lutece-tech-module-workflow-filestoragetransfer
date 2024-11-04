<@formGroup labelFor='entryCode' labelKey='#i18n{module.workflow.filestoragetransfer.manage_taskfiletransferconfigs.columnCode}' mandatory=true>
    <@input type='text' id='entryCode' name='entryCode' maxlength=255 value='${config.entryCode!\'\'}' tabIndex='1' />
</@formGroup>
<@formGroup labelFor='targetFileserviceproviderName' labelKey='#i18n{module.workflow.filestoragetransfer.manage_taskfiletransferconfigs.columnTargetFileserviceproviderName}' mandatory=true>
    <@select name='targetFileserviceproviderName'>
            <#list file_services_list as fileservice>
                <#if fileservice == config.targetFileserviceproviderName!"">
                    <@option value=fileservice label=fileservice selected=true />
                <#else>
                    <@option value=fileservice label=fileservice />
                </#if>
            </#list>
    </@select>
</@formGroup>
<@formGroup labelFor='request_context' labelKey='#i18n{module.workflow.filestoragetransfer.manage_taskfiletransferconfigs.columnContext}' mandatory=true>
    <@input type='text' id='context' name='context' maxlength=255 value='${config.context!\'\'}' tabIndex='2' />
</@formGroup>
