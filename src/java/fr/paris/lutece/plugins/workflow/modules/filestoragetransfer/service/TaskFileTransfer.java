package fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.filestoragetransfer.business.FileTransferRequestHome;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponse;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponseHome;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.service.file.GenericAttributeFileService;
import fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.business.TaskFileTransferConfig;
import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.filestoragetransfer.business.FileTransferRequest;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;



public class TaskFileTransfer extends SimpleTask {

    // Variables
    public static final String TASK_TYPE = "FileTransferTask";
    public static final String TASK_TYPE_LABEL = "File Transfer Task";

    // SERVICES
    @Inject
    @Named("workflow-filestoragetransfer.taskFileTransferService")
    private ITaskConfigService _taskConfigService;


    @Override
    public void init() {
        // Implement the initialization of the task here
    }

    @Override
    public boolean processTaskWithResult(  int nIdResource, String strResourceType, int nIdResourceHistory, HttpServletRequest request, Locale locale, User user  ) 
    {
        //TODO: This task should be re-written to not have any dependencies on the forms plugin

        // Retrieve the configuration
        TaskFileTransferConfig config = _taskConfigService.findByPrimaryKey( this.getId( ) );
        
        // Retrieve the file key
        List<FormQuestionResponse> listFormQuestionResponse = FormQuestionResponseHome.getFormQuestionResponseListByFormResponse(nIdResource);

        // Filter the list to get the entry response with the entry code
        List<Response> listResponse = new ArrayList<Response>();


        listFormQuestionResponse
        .stream().filter(fqr -> fqr.getQuestion().getCode().equals(config.getEntryCode()))
        .forEach(fqr -> listResponse.addAll(fqr.getEntryResponse()));
        
        if(listResponse.size() != 1 ) {
            return false;
        }
        else {  
            Response response = listResponse.get(0);
            String currentFileStoreServiceProvider = GenericAttributeFileService.getInstance().getFileStoreProviderName( response.getFile().getOrigin() );

            FileTransferRequest fileRequestTransfer = new FileTransferRequest( response.getFile().getFileKey(), currentFileStoreServiceProvider, 
                config.getTargetFileserviceproviderName(), config.getContext(), "" );

            FileTransferRequestHome.create( fileRequestTransfer );

            return true;
        }
    }

    @Override
    public String getTitle(Locale locale) {
        return TASK_TYPE_LABEL;
    }
}