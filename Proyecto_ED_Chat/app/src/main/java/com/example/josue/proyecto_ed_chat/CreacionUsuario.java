package com.example.josue.proyecto_ed_chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreacionUsuario extends AppCompatActivity {

    EditText Name;
    EditText Lastname;
    EditText Birthday;
    EditText Phone;
    EditText Email;
    EditText UserName;
    EditText Password;
    Button BotonRegistrarme;
    //consumibles rest api
    JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_creacion_usuario);

        //INSTANCIA RETROFIT
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.233:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Name = findViewById(R.id.txtNombre);
        Lastname = findViewById(R.id.txtApellido);
        Birthday = findViewById(R.id.txtFecha);
        Phone = findViewById(R.id.txtTelefono);
        Email = findViewById(R.id.txtEmail);
        UserName = findViewById(R.id.txtNombreUsuario);
        Password = findViewById(R.id.txtPassword);
        BotonRegistrarme = findViewById(R.id.btnRegistro);

        try
        {
            BotonRegistrarme.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Usuario NewUser = new Usuario(Name.getText().toString(),Lastname.getText().toString(),Birthday.getText().toString(),Phone.getText().toString(),Email.getText().toString(),UserName.getText().toString(),Password.getText().toString());
                    ValidarEmail(NewUser);
                }
            });

        }catch(Exception ex){

            Toast.makeText(getApplication().getBaseContext(),"Error, no se han ingresado los datos necesarios para el registro",Toast.LENGTH_SHORT).show();
        }


    }

    public void Redireccionar(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    private void RegistrarUsuario(Usuario NuevoUsuario){
        Call<ResponseBody> call = jsonPlaceHolderApi.POSTUser(NuevoUsuario);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplication().getBaseContext(),"Error, el Usuario que intentas registrar ya existe " + response.code(),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplication().getBaseContext(),"Usuario Registrado con Exito",Toast.LENGTH_SHORT).show();
                    //Se redirecciona al login para que se puedan logear
                    Redireccionar();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplication().getBaseContext(),"Error " + t.getMessage() ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ValidarEmail(final Usuario NuevoUsuario)
    {
        Call<ResponseBody> call = jsonPlaceHolderApi.ValidarEmail(NuevoUsuario);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplication().getBaseContext(),"Ya existe un Usuario Registrado con este Email, Porfavor ingrese otro",Toast.LENGTH_SHORT).show();
                }else{
                    RegistrarUsuario(NuevoUsuario);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplication().getBaseContext(),"Error " + t.getMessage() ,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
