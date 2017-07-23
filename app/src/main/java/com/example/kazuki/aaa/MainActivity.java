package com.example.kazuki.aaa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang.RandomStringUtils;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    EditText address;
    CheckBox capital;
    CheckBox sign;
    EditText number;
    TextView textView;
    TextView textView6;

    String capitalLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String smallLetter = "abcdefghijklmnopqrstuvwxyz";
    String numberLetter = "0123456789";
    String signLetter = "@*+-/¥";

    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        address = (EditText) findViewById(R.id.address);
        capital = (CheckBox) findViewById(R.id.capital);
        sign = (CheckBox) findViewById(R.id.sign);
        number = (EditText) findViewById(R.id.number);
        textView = (TextView)findViewById(R.id.textView);
        textView6 = (TextView)findViewById(R.id.textView6);


        // Realm全体の初期化
        Realm.init(this);

        // Realmインスタンスを取得
        realm = Realm.getDefaultInstance();


        //realmに保存されているすべての情報を取得する
        RealmResults<UserData> realmResults = realm.where(UserData.class).findAll();

        //userDataにrealmResultsの情報を一つずつ入れていく
        for (UserData userData : realmResults){

            //userDataのメールアドレスとパスワードをlogだしする
            Log.e(userData.getAddress(),userData.getPassword());
        }
    }

    public void generate(View v) {

        //アドレス
        String addressText = address.getText().toString();

        //パスワードの材料となる文字列
        String text = "";

        //パスワード
        String password = "";

        //IDがcapitalであるcheckboxの状態を表す変数
        boolean isCapital;

        //IDがsignであるcheckboxの状態を表す変数
        boolean isSign;

        //パスワードの文字数
        int length;

        //IDがcapitalであるcheckboxの状態を取得する
        isCapital = capital.isChecked();

        //IDがsignであるcheckboxの状態を取得する
        isSign = sign.isChecked();

        //ユーザーが入力した整数を代入
        length = Integer.parseInt(number.getText().toString());


        if (isCapital && isSign) {

            /*大文字と記号を含むパスワードの生成*/

            //パスワード中に大文字を含むかどうかの判定結果を表す変数
            boolean isSearchCapital = false;

            //パスワード中に記号を含むかどうかの判定結果を表す変数
            boolean isSeachSign = false;

            //パスワードの材料となる文字列の生成
            text = capitalLetter + smallLetter + numberLetter + signLetter;

            while (!isSearchCapital || !isSeachSign) {

                /*大文字または記号が含まれていない場合に実行される。*/
                /*パスワードを生成して大文字または記号が含まれているかを判定する*/
                password = RandomStringUtils.random(length, text.toCharArray());
                isSearchCapital = check(password, capitalLetter);
                isSeachSign = check(password, signLetter);
                Log.d("途中経過", password);
            }

            Log.d("パスワード", password);


        } else if (isCapital && !isSign) {

            boolean isSearchCaptal = false;
            text = capitalLetter + smallLetter + numberLetter;

            while (!isSearchCaptal) {
                password = RandomStringUtils.random(length, text.toCharArray());
                isSearchCaptal = check(password, capitalLetter);
                Log.d("途中経過", password);
            }
            Log.d("パスワード", password);


        } else if (!isCapital && isSign) {

            boolean isSearchSign = false;
            text = smallLetter + numberLetter + signLetter;

            while (!isSearchSign) {
                password = RandomStringUtils.random(length, text.toCharArray());
                isSearchSign = check(password, signLetter);
                Log.d("途中経過", password);
            }
            Log.d("パスワード", password);

        } else {
            text = smallLetter + numberLetter;
            password = RandomStringUtils.random(length, text.toCharArray());
            Log.d("パスワード", password);
        }


        //アドレス情報
        final String addressInfo = addressText;
        //パスワード情報
        final String passwordInfo = password;

        //データの書き込みを行う
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //UserDataのID
                int id = 0;

                //一番IDの大きいものを検索
                RealmResults<UserData> result = realm.where(UserData.class).findAll().sort("id");

                //一番最初以外は、一番IDの大きいデータのID+1を新しいIDにする。
                if (result.size() != 0) {
                    id = result.last().getId() + 1;
                }

                //userData型の変数を用意する
                UserData userData = realm.createObject(UserData.class,id);

                //メールアドレスを保存
                userData.setAddress(addressInfo);

                //パスワードを保存
                userData.setPassword(passwordInfo);

            }
        });
            //Intent intent = new Intent(this,Main2Activity.class);
            //startActivity(intent);

            //textView.setText((CharSequence) address);
            //textView6.setText(password);
    }


    public boolean check(String pass, String chkText) {

        //passの中にchkTextの文字列があるかどうかを判定
        boolean result = false;

        //chkTextの文字数だけfor文を回す
        for (int i = 0; i < chkText.toCharArray().length; i++) {

            //chkTextのi番目の文字列を表す
            char charText = chkText.toCharArray()[i];

            //passの中にcharTextがあるかどうかを判定
            if (pass.indexOf(charText) != -1) {
                result = true;
                break;

            }
        }

        return result;
    }

}
