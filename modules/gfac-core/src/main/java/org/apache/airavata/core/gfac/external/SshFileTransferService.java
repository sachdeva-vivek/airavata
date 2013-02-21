package org.apache.airavata.core.gfac.external;

import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.sftp.SftpFile;
import org.apache.airavata.core.gfac.context.invocation.ExecutionContext;
import org.apache.airavata.core.gfac.context.invocation.InvocationContext;
import org.apache.airavata.core.gfac.context.security.impl.SSHSecurityContextImpl;
import org.apache.airavata.core.gfac.exception.GfacException;
import org.apache.airavata.core.gfac.utils.GfacUtils;
import org.apache.airavata.schemas.wec.ContextHeaderDocument;
import org.apache.airavata.schemas.wec.SecurityContextDocument;
import org.apache.airavata.core.gfac.context.invocation.ExecutionContext;
import org.apache.axiom.om.OMElement;
import org.apache.xmlbeans.XmlException;
import org.ietf.jgss.GSSCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xsul.MLogger;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * This class provides SSH based remote file operations. It needs ssh keys to be
 * setup to autenticate with remote host. It uses
 * SSH_USERNAME,SSH_PASSWD,SSH_KEYFILE,SSH_KNOWN_HOSTFILE from gfac options.
 * However if they does not present it uses ${user.name}, and
 * ${user.home}/.ssh/id_rsa as default. If you have setup ssh keys and they do
 * not need a password, then you do not need to do any configuration at all.
 *
 */
public class SshFileTransferService implements FileTransferService {
    public static final Logger log = LoggerFactory.getLogger(SshFileTransferService.class);
    private String username;

    private String password;

    private String keyFileName;

    private String knownHostsFileName;

    private ExecutionContext executionContext;

    private static final String SSH_SECURITY_CONTEXT = "ssh";

    public SshFileTransferService(InvocationContext invocationContext) {
        // --------------------- testing --------------------------------------------
        ExecutionContext execContext = invocationContext.getExecutionContext();
        OMElement omSecurityContextHeader = execContext.getSecurityContextHeader();

        ContextHeaderDocument document = null;
        try {
            document = ContextHeaderDocument.Factory.parse(omSecurityContextHeader.toStringWithConsume());
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (XmlException e) {
            e.printStackTrace();
        }
        SecurityContextDocument.SecurityContext.AmazonWebservices amazonWebservices =
                document.getContextHeader().getSecurityContext().getAmazonWebservices();

        //            TODO
/*
        // MMEEE
        sshContext = ((SSHSecurityContextImpl) context.getSecurityContext(SSH_SECURITY_CONTEXT));
        // MMEEE


        this.executionContext = invocationContext.getExecutionContext();
        GlobalConfiguration configuration = invocationContext.getGlobalConfig();
        username = configuration.getProperty(SSH_USERNAME);
        password = configuration.getProperty(SSH_PASSWD);
        keyFileName = configuration.getProperty(SSH_KEYFILE);
        knownHostsFileName = configuration.getProperty(SSH_KNOWN_HOSTFILE);

        if (username == null) {
            username = System.getProperty("user.name");
        }
        if (keyFileName == null) {
            keyFileName = System.getProperty("user.home") + "/.ssh/id_rsa";
        }*/
    }

    public SshFileTransferService(InvocationContext invocationContext, String username, String keyFileName) {
        this(invocationContext);
        this.username = username;
        this.password = null;
        this.keyFileName = keyFileName;
        this.knownHostsFileName = null;
    }

    public URI forcedCopy(URI src, URI dest, GSSCredential gssCred) throws GfacException {
        return copy(src, dest, gssCred);
    }

    public URI copy(URI src, URI dest, GSSCredential gssCred) throws GfacException {
        log.info("Copying " + src + "->" + dest);
        SshClient sshClient = null;
        SshClient sshClient2 = null;
        try {
            String srchost = src.getHost();
            String srcfile = src.getPath();
            String desthost = dest.getHost();
            String destfile = dest.getPath();

            if (GfacUtils.isLocalHost(srchost) && GfacUtils.isLocalHost(desthost)) {
                FileInputStream in = new FileInputStream(srcfile);
                FileOutputStream out = new FileOutputStream(destfile);
                byte[] buf = new byte[1024];
                int read;
                while ((read = in.read(buf)) > 0) {
                    out.write(buf, 0, read);
                }
                out.close();
                in.close();
            } else if (GfacUtils.isLocalHost(srchost)) {
                sshClient = SSHClient.loginToServer(username, desthost, password, keyFileName,
                        knownHostsFileName);
                SftpClient sftpClient = sshClient.openSftpClient();
                sftpClient.put(srcfile, destfile);
            } else if (GfacUtils.isLocalHost(desthost)) {
                sshClient = SSHClient.loginToServer(username, srchost, password, keyFileName,
                        knownHostsFileName);
                SftpClient sftpClient = sshClient.openSftpClient();
                sftpClient.get(srcfile, destfile);
            } else {
                File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()
                        + new Random().nextLong()), "temp");
                sshClient = SSHClient.loginToServer(username, srchost, password, keyFileName,
                        knownHostsFileName);
                SftpClient sftpClient1 = sshClient.openSftpClient();
                sftpClient1.get(srcfile, tempFile.getAbsolutePath());
                sftpClient1.quit();

                sshClient2 = SSHClient.loginToServer(username, desthost, password, keyFileName,
                        knownHostsFileName);
                SftpClient sftpClient2 = sshClient2.openSftpClient();
                sftpClient2.put(tempFile.getAbsolutePath(), destfile);
                sftpClient2.quit();
                tempFile.delete();
            }
        } catch (FileNotFoundException e) {
//            TODO
//            throw new FileTransferFault(e,FileTransferServiceType.SSH,executionContext.getGlobalConfig().getLocalHost(),src,dest,"");
        } catch (IOException e) {
//            TODO
//            throw new FileTransferFault(e,FileTransferServiceType.SSH,executionContext.getGlobalConfig().getLocalHost(),src,dest,"");
        } finally {
            if (sshClient != null) {
                sshClient.disconnect();
            }
            if (sshClient2 != null) {
                sshClient2.disconnect();
            }
        }
        return dest;
    }

    public URI copyToDir(URI src, URI destDir, GSSCredential gssCred) throws GfacException {
        try {
            URI destFile = new URI(destDir.toString() + "/" + new File(src.getPath()).getName());
            return copy(src, destFile, gssCred);
        } catch (URISyntaxException e) {
            //            TODO
//            throw new GfacException(e,FaultCode.InvaliedLocalArgumnet);
        }
        //            TODO : Remove
        return null;
    }

    public URI copyWithDataID(DataIDType src, URI destURL, GSSCredential gssCred)
            throws GfacException {
        throw new UnsupportedOperationException();
    }

    public boolean isExisits(URI srcURI, GSSCredential gssCred) throws GfacException {
        String desthost = srcURI.getHost();
        String destfile = srcURI.getPath();

        File destFileAsFile = new File(destfile);
        String fileNamePart = destFileAsFile.getName();
        String filepathPart = destFileAsFile.getParent();

        SshClient sshClient = SSHClient.loginToServer(username, desthost, password, keyFileName,
                knownHostsFileName);
        try {
            SftpClient sftpClient = sshClient.openSftpClient();
            List<SftpFile> files = (List<SftpFile>) sftpClient.ls(filepathPart);
            for (SftpFile file : files) {
                String returnedFileName = file.getFilename().replaceAll("/", "");
                if (returnedFileName.equals(fileNamePart)) {
                    return true;
                }
            }
            return files.contains(fileNamePart);
        } catch (IOException e) {
            //            TODO
//            throw new FileSystemFault(e,FileTransferServiceType.SSH,"exits",srcURI.toString());
        } finally {
            if (sshClient != null) {
                sshClient.disconnect();
            }
        }
        //            TODO : Remove
        return false;
    }

    public Vector<URI> listDir(URI srcURI, GSSCredential gssCred) throws GfacException {
        String desthost = srcURI.getHost();
        String destfile = srcURI.getPath();
        SshClient sshClient = SSHClient.loginToServer(username, desthost, password, keyFileName,
                knownHostsFileName);
        try {
            SftpClient sftpClient = sshClient.openSftpClient();
            List files = sftpClient.ls(destfile);
            if (files != null) {
                Vector<URI> results = new Vector<URI>();
                for (Object sftpfile : files) {
                    String filePart = ((SftpFile) sftpfile).getFilename();
                    if (!filePart.endsWith(".")) {
                        results.add(new URI(srcURI.toString() + "/" + filePart));
                    }
                }
                return results;
            }
            return null;
        } catch (IOException e) {
            //            TODO
//            throw new FileSystemFault(e,FileTransferServiceType.SSH,"listDir",srcURI.toString());
        } catch (URISyntaxException e) {
            //            TODO
            // throw new GfacException(e,FaultCode.InvaliedLocalArgumnet);
        } finally {
            if (sshClient != null) {
                sshClient.disconnect();
            }
        }
        //            TODO : Remove
        return null;
    }

    public void makeDir(URI destURI, GSSCredential gssCred) throws GfacException {
        String desthost = destURI.getHost();
        String destfile = destURI.getPath();
        log.info("SFTP to Host:" + desthost);
        log.info("SFTP to Dir:" + destfile);
        SshClient sshClient = SSHClient.loginToServer(username, desthost, password, keyFileName,
                knownHostsFileName);
        try {
            SftpClient sftpClient = sshClient.openSftpClient();
            sftpClient.mkdir(destfile);
            log.info("Create directory " + destURI);
        } catch (IOException e) {
            //            TODO
            // throw new FileSystemFault(e,FileTransferServiceType.SSH,"mkdir",destURI.toString());
        } finally {
            if (sshClient != null) {
                sshClient.disconnect();
            }
        }
    }

    public String readRemoteFile(URI destURI, GSSCredential gsCredential,
                                 File localFile) throws GfacException {
        SshClient sshClient1 = null;
        SftpClient sftpClient1 = null;
        String fileAsStr = null;
        try {
            String desthost = destURI.getHost();
            String destfile = destURI.getPath();

            File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()
                    + new Random().nextLong()), "temp");
            sshClient1 = SSHClient.loginToServer(username, desthost, password, keyFileName,
                    knownHostsFileName);
            sftpClient1 = sshClient1.openSftpClient();
            sftpClient1.get(destfile, tempFile.getAbsolutePath());
            sftpClient1.quit();
            sshClient1.disconnect();
            //            TODO
//            fileAsStr = GfacUtils.readFile(tempFile.getAbsolutePath());
            tempFile.delete();
        } catch (IOException e) {
            //            TODO
            // throw new FileSystemFault(e,FileTransferServiceType.SSH,"readRemoteFile",destURI.toString());
        } finally {
            try {
                if (sftpClient1 != null) {
                    sftpClient1.quit();
                }
                if (sshClient1 != null) {
                    sshClient1.disconnect();
                }
            } catch (IOException e) {
            }
        }
        return fileAsStr;
    }

    public DataIDType store(URI src) throws GfacException {
        throw new UnsupportedOperationException();
    }

    public ContactInfo findContact(URI uri) throws GfacException {
        return new ContactInfo(uri.getHost(),uri.getPort());
    }

    public URI[] copyToDir(URI[] srcList, URI destURL, GSSCredential gssCred) throws GfacException {
        URI[] destFiles = new URI[srcList.length];
        for (int i = 0; i < srcList.length; i++) {
            destFiles[i] = copy(srcList[i], destURL, gssCred);
        }
        return destFiles;
    }

}