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
            responseFilter.setFileKey(fileTransferRequest.getSourceFileKey());

            List<Response> listResponse = ResponseHome.getResponseList( responseFilter );
            Response response = null;
            if ( listResponse != null && !listResponse.isEmpty( ) )
            {
                response = listResponse.get( 0 );
                ResponseHome.updateFileKey( response.getIdResponse(), fileTransferRequest.getTargetFileKey(), fileTransferRequest.getTargetFileserviceproviderName() );
            }
            else {
                throw new IllegalStateException( "No response found for file key " + fileTransferRequest.getSourceFileKey() );
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