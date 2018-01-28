package com.github.tinkerti.ziwu;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.facebook.stetho.inspector.database.DefaultDatabaseConnectionProvider;
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsDomain;
import com.github.tinkerti.ziwu.data.TaskManager;
import com.github.tinkerti.ziwu.stetho.TMDatabaseDriver;
import com.github.tinkerti.ziwu.stetho.TMDatabaseFilesProvider;
import com.github.tinkerti.ziwu.stetho.TMDbFilesDumperPlugin;

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initialize(new Stetho.Initializer(this) {
            @Override
            protected Iterable<DumperPlugin> getDumperPlugins() {
                return new Stetho.DefaultDumperPluginsBuilder(App.this)
                        .provide(new TMDbFilesDumperPlugin(App.this, new TMDatabaseFilesProvider(App.this)))
                        .finish();
            }

            @Override
            protected Iterable<ChromeDevtoolsDomain> getInspectorModules() {
                Stetho.DefaultInspectorModulesBuilder defaultInspectorModulesBuilder = new Stetho.DefaultInspectorModulesBuilder(App.this);
                defaultInspectorModulesBuilder.provideDatabaseDriver(new TMDatabaseDriver(App.this, new TMDatabaseFilesProvider(App.this), new DefaultDatabaseConnectionProvider()));
                return defaultInspectorModulesBuilder.finish();
            }
        });

        TaskManager.init(this);
        TaskManager.getInstance().loginSuccess(this,"userId");

    }
}
