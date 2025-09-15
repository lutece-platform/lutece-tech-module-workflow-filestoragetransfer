package fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.business.EntryFilter;
import fr.paris.lutece.plugins.genericattributes.business.EntryHome;
import fr.paris.lutece.plugins.genericattributes.business.EntryType;
import fr.paris.lutece.plugins.genericattributes.business.EntryTypeHome;
import fr.paris.lutece.plugins.genericattributes.service.GenericAttributesPlugin;
import fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.business.TaskFileTransferConfig;
import fr.paris.lutece.plugins.workflow.web.task.AbstractTaskComponent;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfig;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.portal.service.rbac.ResourceTypeProvider;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

public class TaskFileTransferTaskComponent extends AbstractTaskComponent {


    private static final String MARK_CONFIG = "config";
    private static final String MARK_FILESERVICES_LIST = "file_services_list";

    /*
    private static final String MARK_LIST_ENTRIES = "list_entries";
    private static final String MARK_LIST_RESOURCE_TYPE = "list_resource_type";
    private static final String MARK_LIST_RESOURCE_CODE = "list_resource_code";


    // Parameters
    private static final String PARAMETER_ACTION = "apply";
    private static final String PARAMETER_RESOURCE_TYPE = "resource_type";
    private static final String PARAMETER_RESOURCE_CODE = "resource_code";
    private static final String PARAMETER_ENTRY_CODE = "entry_code";


    // Action
    private static final String ACTION_SELECT_RESOURCE_TYPE = "select_resource_type";
    private static final String ACTION_SELECT_RESOURCE_CODE = "select_resource_code";
    private static final String ACTION_SELECT_ENTRY_CODE = "select_entry_code";
    */
    
    private static final String FILTER_RESOURCE_TYPE = "FORMS_FORM";
    //private static final int FILTER_ENTRY_TYPE = 8;

    @Inject
    @Named( "workflow-filestoragetransfer.taskFileTransferService" )
    private ITaskConfigService _taskConfigService;

    private static final String TEMPLATE_TASK_FILE_STORAGE_TRANSFER_CONFIG = "admin/plugins/workflow/modules/filestoragetransfer/task_file_storage_transfer_config_form.ftl";
    private static final String TEMPLATE_TASK_FILE_STORAGE_TRANSFER = "admin/plugins/workflow/modules/filestoragetransfer/task_file_storage_transfer_form.ftl";

    TaskFileTransferConfig _config;

    @Override
    public String getDisplayTaskForm(int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task) 
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_CONFIG, _taskConfigService.findByPrimaryKey( task.getId( ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_FILE_STORAGE_TRANSFER, locale, model );

        return template.getHtml( );
    }

    @Override
    public String getDisplayConfigForm(HttpServletRequest request, Locale locale, ITask task) 
    {
        // TODO :
        // Modify this code to filter on different ResourceType linked to GenericAttributes
        // Modify this code to provide a list of resources (forms, appointments) to select the appropriate entry

            TaskFileTransferConfig config = _taskConfigService.findByPrimaryKey( task.getId( ) ) == null 
            ? new TaskFileTransferConfig( ) 
            : _taskConfigService.findByPrimaryKey( task.getId( ) );

            List<String> listFileServices = SpringContextService.getBeansOfType( IFileStoreServiceProvider.class )
            .stream( ).map( IFileStoreServiceProvider::getName ).collect( Collectors.toList( ) );
            
            Map<String, Object> model = new HashMap<>( );
            model.put ( MARK_FILESERVICES_LIST, listFileServices );
            model.put( MARK_CONFIG, config );

            final HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_FILE_STORAGE_TRANSFER_CONFIG, locale, model );
            return template.getHtml( );
    }

    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task ) 
    {
        _config = _taskConfigService.findByPrimaryKey( task.getId( ) );
       
        boolean create = _config == null;
        if ( create )
        {
            _config = new TaskFileTransferConfig( );
            _config.setIdTask( task.getId( ) );
            _config.setTargetFileserviceproviderName( request.getParameter( "targetFileserviceproviderName" ) );
            _config.setContext( AppPropertiesService.getProperty( "workflow-filestoragetransfer.filetransfercontext" ) );
            _config.setEntryCode( request.getParameter( "entryCode" ) );
            _config.setResourceCode( "" );
            _config.setResourceType( FILTER_RESOURCE_TYPE );

            _taskConfigService.create( _config );
        }
        else
        {
            _config.setEntryCode( request.getParameter( "entryCode" ) );
            _config.setTargetFileserviceproviderName( request.getParameter( "targetFileserviceproviderName" ) );
            _taskConfigService.update( _config );
        }
        return null;

    }

    @Override
    public String getDisplayTaskInformation(int nIdHistory, HttpServletRequest request, Locale locale, ITask task) {
        TaskFileTransferConfig config = _taskConfigService.findByPrimaryKey( task.getId( ) ) == null 
        ? new TaskFileTransferConfig( ) 
        : _taskConfigService.findByPrimaryKey( task.getId( ) );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_CONFIG, config );
        
        return AppTemplateService.getTemplate( TEMPLATE_TASK_FILE_STORAGE_TRANSFER, locale, model ).getHtml( );
    }

    @Override
    public String doValidateTask(int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task) {
        return null;
    }
}
