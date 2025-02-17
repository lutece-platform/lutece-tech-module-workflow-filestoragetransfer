<@formGroup labelFor='entryCode' labelKey='#i18n{module.workflow.filestoragetransfer.manage_taskfiletransferconfigs.columnCode}'>
    <@input type='text' id='entryCode' name='entryCode' maxlength=255 value='${config.entryCode!\'\'}' tabIndex='1' readonly=true/>
</@formGroup>
<@formGroup labelFor='entryCode' labelKey='#i18n{module.workflow.filestoragetransfer.manage_taskfiletransferconfigs.columnTargetFileserviceproviderName}'>
    <@input type='text' id='entryCode' name='entryCode' maxlength=255 value='${config.targetFileserviceproviderName!\'\'}' tabIndex='2' readonly=true/>
</@formGroup>
