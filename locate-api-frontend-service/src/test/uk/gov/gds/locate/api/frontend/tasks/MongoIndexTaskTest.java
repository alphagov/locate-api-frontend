package uk.gov.gds.locate.api.frontend.tasks;

import com.google.common.collect.ImmutableMultimap;
import org.junit.Test;
import uk.gov.gds.locate.api.frontend.dao.AuthorizationTokenDao;

import java.io.PrintWriter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MongoIndexTaskTest {

    private AuthorizationTokenDao authorizationTokenDao = mock(AuthorizationTokenDao.class);

    @Test
    public void shouldIndexBothUsageAndAuthCollections() throws Exception {
        MongoIndexTask task = new MongoIndexTask(authorizationTokenDao);
        task.execute(ImmutableMultimap.<String, String>of("a","b"), new PrintWriter("test"));
        verify(authorizationTokenDao, times(1)).applyIndexes();
    }

}
