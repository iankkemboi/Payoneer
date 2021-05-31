package com.ian.payment.payoneer;




import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ian.payment.payoneer.model.ListResult;
import com.ian.payment.payoneer.repository.PayMethodsRepo;
import com.ian.payment.payoneer.ui.ListResultViewState;
import com.ian.payment.payoneer.viewmodel.PaymentMethodsViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

    @RunWith(JUnit4.class)
    public class PayMethodsViewModelUnitTest {
        @Rule
        public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
        public static final String  ASSET_BASE_PATH = "../app/src/main/assets/";


        @Mock
        PayMethodsRepo repository;
        private PaymentMethodsViewModel viewModel;
        @Mock
        Observer<ListResultViewState> observer;
        @Mock
        Observable<ListResult> listresultvalue;
        @Mock
        LifecycleOwner lifecycleOwner;
        Lifecycle lifecycle;

        @BeforeClass
        public static void setUpClass() {

            RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> Schedulers.trampoline());
        }

        @Before
        public void setUp() throws Exception {
            MockitoAnnotations.initMocks(this);

            lifecycle = new LifecycleRegistry(lifecycleOwner);
            viewModel = new PaymentMethodsViewModel(repository);
            viewModel.getListResultState().observeForever(observer);
        }

        @Test
        public void testNull() {
            when(repository.getPayMethods()).thenReturn(null);
            assertNotNull(viewModel.getListResultState());
            assertTrue(viewModel.getListResultState().hasObservers());
        }


        @Test
        public void testApiFetchDataSuccess() throws IOException {
            // Mock API response
            Gson gson = new GsonBuilder().create();
            ListResult listres = gson.fromJson(readJsonFile("listresult.json"),
                    ListResult.class);

            when(repository.getPayMethods()).thenReturn(Observable.just(listres));
            viewModel.getPayMethods();
            verify(observer).onChanged(ListResultViewState.LOADING_STATE);
            verify(observer).onChanged(ListResultViewState.SUCCESS_STATE);
        }
        public String readJsonFile (String filename) throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ASSET_BASE_PATH + filename)));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }

            return sb.toString();
        }

        @Test
        public void testApiFetchDataError() {
            when(repository.getPayMethods()).thenReturn(Observable.error(new Throwable("Api error")));
            viewModel.getPayMethods();
            verify(observer).onChanged(ListResultViewState.LOADING_STATE);
            verify(observer).onChanged(ListResultViewState.ERROR_STATE);
        }

        @Test
        public void testApiFetchDataCheckEmpty() throws IOException {
            // Mock API response
            Gson gson = new GsonBuilder().create();
            ListResult listres = gson.fromJson(readJsonFile("listresultempty.json"),
                    ListResult.class);

            when(repository.getPayMethods()).thenReturn(Observable.just(listres));
            viewModel.getPayMethods();
            verify(observer).onChanged(ListResultViewState.LOADING_STATE);
            verify(observer).onChanged(ListResultViewState.EMPTY_STATE);
        }


        @After
        public void tearDown() throws Exception {
            repository = null;
            viewModel = null;
        }

}
