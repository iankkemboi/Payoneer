package com.ian.payment.payoneer.viewmodel;

import com.ian.payment.payoneer.model.ListResult;
import com.ian.payment.payoneer.repository.PayMethodsRepo;
import com.ian.payment.payoneer.ui.ListResultViewState;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


@HiltViewModel
public class PaymentMethodsViewModel extends ViewModel {
    private CompositeDisposable disposable;

    private static final String TAG = "PayMethodsViewModel";

    private PayMethodsRepo payMethodsRepo;

    private final MutableLiveData<ListResultViewState> listresultstate = new MutableLiveData<>();

    public MutableLiveData<ListResultViewState> getListResultState() {
        return listresultstate;
    }


    @Inject
    public PaymentMethodsViewModel(PayMethodsRepo payMethodsRepo) {

        this.payMethodsRepo = payMethodsRepo;
        disposable = new CompositeDisposable();
    }


    public void getPayMethods() {
        listresultstate.postValue(ListResultViewState.LOADING_STATE);
        disposable.add(payMethodsRepo.getPayMethods()
                .subscribeOn(Schedulers.io())

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError
                ));

    }

    private void onSuccess(ListResult listres) {
        if (listres.getNetworks().getApplicable().isEmpty()) {

            listresultstate.postValue(ListResultViewState.EMPTY_STATE);
        } else {
            ListResultViewState.SUCCESS_STATE.setData(listres);
            listresultstate.postValue(ListResultViewState.SUCCESS_STATE);
        }
    }

    private void onError(Throwable error) {
        ListResultViewState.ERROR_STATE.setError(error);
        listresultstate.postValue(ListResultViewState.ERROR_STATE);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.clear();
            disposable = null;
        }
    }
}
