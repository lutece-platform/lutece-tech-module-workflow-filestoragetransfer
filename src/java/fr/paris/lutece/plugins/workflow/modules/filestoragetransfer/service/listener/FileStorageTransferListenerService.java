package fr.paris.lutece.plugins.workflow.modules.filestoragetransfer.service.listener;

import java.util.List;

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.plugins.filestoragetransfer.business.FileStorageTransferRequest;
import fr.paris.lutece.plugins.filestoragetransfer.service.listener.IFileStorageTransferListener;

import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.business.ResponseFilter;
import fr.paris.lutece.plugins.genericattributes.business.ResponseHome;

public class FileStorageTransferListenerService implements IFileStorageTransferListener
{
    private static String contextValue = AppPropertiesService.getProperty( "workflow-filestoragetransfer.filetransfercontext" );

    @Override
    public void changeFileService( FileStorageTransferRequest fileTransferRequest )
    {
        if ( fileTransferRequest == null )
        {
            return;
        }
        else if( !verifyFileStorageTransferContext( fileTransferRequest.getRequestContext( ) ) )
        {
            return;
        }
        else {
            ResponseFilter responseFilter = new ResponseFilter();
            List<Response> responseList = ResponseHome.getResponseList( responseFilter );

            Response response = responseList.stream().filter( r -> r.getFile().getFileKey().equals( fileTransferRequest.getSourceFileKey() ) ).findFirst().orElse( null );

            if ( response != null )
            {
                ResponseHome.updateFileKey( response.getIdResponse(), fileTransferRequest.getTargetFileKey(), fileTransferRequest.getTargetFileserviceproviderName() );
            }
        }
    }

    @Override
    public boolean verifyFileStorageTransferContext( String fileTransferRequestContext ) {
        return fileTransferRequestContext.equals( contextValue ) ? true : false;
    }

    @Override
    public String getName()
    {
        return "FileTransferListenerService";
    }
}