package com.example.roxanagh.test;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;

/**
 * Created by roxanagh on 3/11/2015.
 */
public class SplashScreenActivity extends Activity {
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        session = new SessionManager(getApplicationContext());
        //TEST//
        User user = new User(1l, "roxana", "roxana", "ghita@gmail.com", UserRole.PRODUCER, new Detail(1l, "Ghita", "Roxana", "0724691892", new County(4l,"B","MUNICIPIUL BUCURESTI"), new Locality(179141l,new County(),"179141","Sector1")));
        Utils.userLogged = user;
        session.createLoginSession(user.getUsername(), user.getPassword(), user.getUserRole());
        //TEST//

        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 1500) {
                        sleep(100);
                        waited += 100;
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    finish();
                    Intent i = new Intent();
                    if (!session.isLoggedIn()) {
                        i.setClassName("com.example.roxanagh.test", "com.example.roxanagh.test.LoginActivity");
                        startActivity(i);
                    } else {
                        int idUserRole = 0;

                        //idUserRole = Integer.parseInt(session.getUserDetails().get(Utils.ID_USER_ROLE));

                        idUserRole = 2;
                        switch (idUserRole) {
                            case 1:
                                i.setClassName("com.example.roxanagh.test", "com.example.roxanagh.test.ProducerMainMenu");
                                startActivity(i);
                                finish();
                                break;
                            case 2:
                                i.setClassName("com.example.roxanagh.test", "com.example.roxanagh.test.BuyerMainMenu");
                                startActivity(i);
                                finish();
                                break;
                            case 3:
                                i.setClassName("com.example.roxanagh.test", "com.example.roxanagh.test.TransporterMainMenu");
                                startActivity(i);
                                finish();
                                break;
                        }
                        // }
                    }
                }
            }
        };
        splashThread.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

}
