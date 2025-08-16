package hudson.plugins.audit_trail;

import hudson.EnvVars;
import java.nio.file.Path;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.jvnet.hudson.test.Issue;

/**
 * Created by Pierre Beitz
 * on 2019-05-05.
 */
public class LogFileAuditLoggerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Issue("JENKINS-56108")
    @Test
    public void configuringAFileLoggerWithNonExistingParents() {
        Path logFile = folder.getRoot().toPath().resolve("subdirectory").resolve("file");
        new LogFileAuditLogger(logFile.toString(), 5, 1, null);
        Assert.assertTrue(logFile.toFile().exists());
    }

    @Issue("JENKINS-67493")
    @Test
    public void environmentVariablesAreProperlyExpanded() {
        Path rootFolder = folder.getRoot().toPath();
        EnvVars.masterEnvVars.put("EXPAND_ME", "expandMe");
        String logFile = rootFolder.resolve("${EXPAND_ME}").toString();
        LogFileAuditLogger logger = new LogFileAuditLogger(logFile, 5, 1, null);
        Assert.assertEquals(rootFolder.resolve("expandMe").toString(), logger.getLog());
    }
}
