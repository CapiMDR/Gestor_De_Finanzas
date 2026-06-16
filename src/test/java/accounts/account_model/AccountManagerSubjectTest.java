package accounts.account_model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AccountManagerSubjectTest {

    @Test
    void testPrivateConstructor() throws Exception {
        java.lang.reflect.Constructor<AccountManagerSubject> constructor = AccountManagerSubject.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    void testObservers() {
        AccountObserver mockObserver = mock(AccountObserver.class);
        
        AccountManagerSubject.addObserver(mockObserver);
        AccountManagerSubject.notifyObservers(new ArrayList<>());
        
        verify(mockObserver, times(1)).onNotify(any());
        
        AccountManagerSubject.removeObserver(mockObserver);
        AccountManagerSubject.notifyObservers(new ArrayList<>());
        
        verify(mockObserver, times(1)).onNotify(any());
    }
}
