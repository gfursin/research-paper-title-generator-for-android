package ctuning.com.researchpapertitlegenerator;

/* Main classes */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import org.ctuning.openme.openme;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/************/

public class MainActivity extends Activity {

    String curl_sr="http://cTuning.org/shared-computing-resources-json/ck.json";
    String curl_ctuning = "http://cTuning.org";
    String curl_vision = "http://arxiv.org/abs/1406.4020";
    String curl_sdk = "http://github.com/ctuning/ck";
    String curl_fursin = "http://fursin.net/research.html";
    String curl_wiki = "http://ctuning.org/cm/wiki/index.php?title=Reproducibility";
    String curl = "http://cknowledge.ddns.net/repo/json.php?"; // CK repository URL
    int delay = 3000;

    Button bb_author;
    Button bb_sdk;
    Button bb_wiki;
    Button bb_vision;
    Button bb_generate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButtons();

        EditText text1 = (EditText) findViewById(R.id.et);
        text1.append("Dear colleagues,\n\n");
        text1.append("  Many researchers are often desperately ");
        text1.append("searching for new research topics in computer engineering ");
        text1.append("to continue publishing numerous papers ");
        text1.append("required for their degree and promotion. ");
        text1.append("  In order to help them, we created this paper title generator ;) ...\n\n");
        text1.append("  Alternatively, you may check out our open research SDK, ");
        text1.append("public repository of knowledge, community driven paper/artifact reviewing, ");
        text1.append("or even join our long-term initiative to radically change ");
        text1.append("current research, development and experimental methodology ");
        text1.append("making it truly collaborative, open and reproducible!\n\n");
        text1.append("Have fun,\n");
        text1.append("  Grigori Fursin\n");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addListenerOnButtons() {
        bb_author = (Button) findViewById(R.id.b_author);
        bb_sdk = (Button) findViewById(R.id.b_sdk);
        bb_wiki = (Button) findViewById(R.id.b_wiki);
        bb_vision = (Button) findViewById(R.id.b_vision);
        bb_generate = (Button) findViewById(R.id.b_generate);

        /********************************************************************************/
        bb_author.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings({"unused", "unchecked"})
            @Override
            public void onClick(View arg0) {
                EditText text1 = (EditText) findViewById(R.id.et);
                text1.append("\nOpening author's page ...\n");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(curl_fursin));
                startActivity(browserIntent);
            }
        });

        /********************************************************************************/
        bb_sdk.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings({"unused", "unchecked"})
            @Override
            public void onClick(View arg0) {
                EditText text1 = (EditText) findViewById(R.id.et);
                text1.append("\nOpening research SDK GITHUB page ...\n");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(curl_sdk));
                startActivity(browserIntent);
            }
        });

        /********************************************************************************/
        bb_vision.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings({"unused", "unchecked"})
            @Override
            public void onClick(View arg0) {
                EditText text1 = (EditText) findViewById(R.id.et);
                text1.append("\nOpening page with our long-term vision paper ...\n");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(curl_vision));
                startActivity(browserIntent);
            }
        });

        /********************************************************************************/
        bb_wiki.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings({"unused", "unchecked"})
            @Override
            public void onClick(View arg0) {
                EditText text1 = (EditText) findViewById(R.id.et);
                text1.append("\nOpening collaborative and reproducible wiki page ...\n");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(curl_wiki));
                startActivity(browserIntent);
            }
        });

        /********************************************************************************/
        bb_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            //FGG: do not add code here - only via runAsync
            //otherwise access to Internet, etc is blocked!
            public void onClick(View arg0) {
                new runAsync().execute("");
            }
        });
    }

    private void alertbox(String title, String mymessage) {
        // TODO Auto-generated method stub
        new AlertDialog.Builder(this)
                .setMessage(mymessage)
                .setTitle(title)
                .setCancelable(true)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                .show();
    }

    private class runAsync extends AsyncTask<String, String, String> {
        protected void onProgressUpdate(String... values) {
            if (values[0] != "") {
                EditText text1 = (EditText) findViewById(R.id.et);
                text1.append(values[0]);
                text1.setSelection(text1.getText().length());
            } else if (values[1] != "") {
                alertbox(values[1], values[2]);
            }
        }

        @Override
        protected String doInBackground(String... arg0) {
            /* Get available/shared CK server */
            publishProgress("\n\n");
            publishProgress("Trying to get available shared CK server (computing resource) from "+curl_sr+" ...\n");
            curl=get_shared_computing_resource(curl_sr);

            publishProgress("\n\n");

            if (curl.startsWith("ERROR")) {
                publishProgress(curl);
                publishProgress("\n");
                return null;
            }
            else
            {
                publishProgress("Shared resource found:\n");
                publishProgress(curl);
                publishProgress("\n");
            }

            publishProgress("\n\nSending request to cKnowledge.org ...\n");

            JSONObject ii = new JSONObject();
            try {
                ii.put("remote_server_url", curl);
                ii.put("action", "generate");
                ii.put("module_uoa", "dissemination.publication");
                ii.put("data_uoa", "template-joke");
                ii.put("out", "json");
            } catch (JSONException e) {
                publishProgress("\nError with JSONObject ...\n\n");
                e.printStackTrace();
                return null;
            }

            JSONObject r = null;

            try {
                r = openme.remote_access(ii);
            } catch (JSONException e) {
                publishProgress("\nError calling OpenME interface (" + e.getMessage() + ") ...\n\n");
                return null;
            }

            Integer rr = 0;
            if (!r.has("return")) {
                publishProgress("\nError obtaining key 'return' from OpenME output ...\n\n");
                return null;
            }
            try {
                rr = (Integer) r.get("return");
            } catch (JSONException e) {
                publishProgress("\nError obtaining key 'return' from OpenME output (" + e.getMessage() + ") ...\n\n");
                return null;
            }

            if ((rr > 0) && (rr != 16)) {
                String err = "";
                try {
                    err = (String) r.get("error");
                } catch (JSONException e) {
                    publishProgress("\nError obtaining key 'error' from OpenME output (" + e.getMessage() + ") ...\n\n");
                    e.printStackTrace();
                    return null;
                }

                publishProgress("\nProblem accessing cknowledge.org: " + err + "\n");
                return null;
            }

            String title = "";
            try {
                title = (String) r.get("string");
            } catch (JSONException e) {
                publishProgress("\nError obtaining key 'string' from OpenME output (" + e.getMessage() + ") ...\n\n");
                e.printStackTrace();
                return null;
            }

            publishProgress("\nGenerated research topic or paper title:\n\n" + title + "\n");

            return null;
        }

        public String get_shared_computing_resource(String url) {
            String s="";

            try
            {
                //Connect
                URL u=new URL(url);

                URLConnection urlc = u.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));

                String line="";
                while ((line = br.readLine()) != null)
                    s+=line+'\n';
                br.close();
            }
            catch(Exception e)
            {
                return "ERROR: "+ e.getMessage();
            }

            /* Trying to convert to dict from JSON */
            JSONObject a=null;

            try {
                a=new JSONObject(s);
            } catch (JSONException e) {
                return "ERROR: Cant' convert string to JSON:\n"+s+"\n("+e.getMessage()+")\n";
            }

            /* For now just take default one, later add random or balancing */
            JSONObject rs=null;
            try {
                if (a.has("default"))
                    rs=(JSONObject) a.get("default");
                if (rs!=null){
                        if (rs.has("url"))
                        s=(String)rs.get("url");
                    }
            } catch (JSONException e) {
                return "ERROR: Cant' convert string to JSON:\n"+s+"\n("+e.getMessage()+")\n";
            }

            if (s==null)
                s="";
            else if (!s.endsWith("?"))
                s+="/?";

            return s;
        }
    }
}
