package com.example.kajol.rxjavasample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import io.reactivex.*;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> arrayList;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Disposable disposable;
    TextView displayItems;
    StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayItems = findViewById(R.id.itemName);
        sb = new StringBuilder();

        printChessPieces();

    }

    public void printChessPieces(){

        Observable<String> chessObservable = Observable.just("Rook","Knight","Bishop","Queen","King","pawn");
        Observer<String> chessObserver = getChessObserver();

        chessObservable.subscribeOn(Schedulers.io())
                       .observeOn(AndroidSchedulers.mainThread())
                       .filter(new Predicate<String>() {
                           @Override
                           public boolean test(String s) throws Exception {
                               return s.startsWith("K");
                           }
                       })
                       .subscribe(chessObserver);


    }

    private Observer<String> getChessObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "Name: " + s);
                sb.append(s);
                sb.append("\n");
                displayItems.setText(sb.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "All items are emitted!");
            }
        };
    }

    @Override
    public void onDestroy(){

        super.onDestroy();
        disposable.dispose();

    }
}
