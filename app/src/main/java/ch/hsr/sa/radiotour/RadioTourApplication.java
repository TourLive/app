package ch.hsr.sa.radiotour;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by Dom on 12.10.2017.
 */

public class RadioTourApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.
                Builder().
                name("radiotour.realm").
                deleteRealmIfMigrationNeeded().
                build();
        Realm.setDefaultConfiguration(config);



        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
    }
}
