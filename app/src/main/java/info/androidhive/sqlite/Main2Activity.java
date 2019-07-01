package info.androidhive.sqlite;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Element versionElement = new Element();
        versionElement.setTitle("Version 1.0");

        Element adsElement = new Element();
        adsElement.setTitle("To do");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("wafaaaaaa")
                .setImage(R.drawable.bell)
                .addItem(versionElement)
                .addItem(adsElement)

                .addGroup("Connect with Wafaa Alayoubi")
                .addEmail("wafaaelayoubi.w@gmail.com")
                .addFacebook("WafaaAyoubi97")
                .addGitHub("WafaaElAyoubi")

                .addGroup("Connect with Ibrahim Isleem")
                .addEmail("ibrahimm.isleem@gmail.com")
                .addFacebook("Ibrahim.Isleem.97")
                .addGitHub("ISLEEM75")
                .create();

        setContentView(aboutPage);
    }


}
