package com.example.josue.proyecto_ed_chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    //consumibles rest api
    JsonPlaceHolderApi jsonPlaceHolderApi;
    Button LoginButton;
    EditText Username;
    EditText Password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        //INSTANCIA DE RETROFIT
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.233:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //INICIALIZACION DE VARIABLES
        Username = findViewById(R.id.Username);
        Password = findViewById(R.id.password);
        LoginButton = findViewById(R.id.signinbutton);
        TextView Registro = findViewById(R.id.txtRegistrate);


        //Se Redirecciona al registro de datos
        Registro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RedireccionarRegistro();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final UsuarioLogin NewUser = new UsuarioLogin(Username.getText().toString(), Password.getText().toString());
                if(Username.getText().toString().equals("") == false && Password.getText().toString().equals("") == false)
                LoginUsuario(NewUser);
                else
                Toast.makeText(getApplication().getBaseContext(),"Error, debes ingresar datos antes de iniciar",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void Redireccionar(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void RedireccionarRegistro(){
        Intent intent = new Intent(this, CreacionUsuario.class);
        startActivity(intent);
    }

    private void LoginUsuario(UsuarioLogin NewUser)
    {
        Call<ResponseBody> call = jsonPlaceHolderApi.Login(NewUser);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplication().getBaseContext(),"Error, No se ha podido iniciar sesion el usuario o la contraseña son incorrectos",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplication().getBaseContext(),"Iniciando Sesion",Toast.LENGTH_SHORT).show();
                    String token = "";
                    try {
                        token = response.body().string();
                        token = token.replace("\"","");
                        token.toCharArray();
                        String Auxiliar = "";
                        for (char i:token.toCharArray())
                        {
                            if(String.valueOf(i).equals("") == false)
                            Auxiliar = Auxiliar + String.valueOf(i);
                        }
                        token = Auxiliar;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Se Genera y Almacena el Token
                    SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
                    SharedPreferences.Editor myEditor = myPreferences.edit();
                    //Se Guarda el Token Obtenido
                    myEditor.putString("Token", token);
                    myEditor.putString("Username",Username.getText().toString());
                    myEditor.commit();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Redireccionar();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplication().getBaseContext(),"Error " + t.getMessage() ,Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void GETUsuarios(){
        Call<List<Usuario>> call = jsonPlaceHolderApi.getUsers();
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getApplication().getBaseContext(),"Code: " + response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Usuario> usuarios = response.body();
                for(Usuario Usuario : usuarios){
                    String content ="";
                    content += "Nombre: " + Usuario.getName() + " Apellido: " +Usuario.getLastname() +"Usuario: "
                            + Usuario.getUsername() + " Contraseña: " + Usuario.getPassword()
                            +  " Correo: " + Usuario.getEmail() + " Cumpleaños: "+ Usuario.getBirthday() + " Telefono: "+Usuario.getPhone();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(getApplication().getBaseContext(),"Error " + t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void VerificarUsuario(String nombreUsuario){
        Call<ResponseBody> call = jsonPlaceHolderApi.getUser(nombreUsuario);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() != 201){
                    Toast.makeText(getApplication().getBaseContext(),"Error " + response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    //Se Genera y Almacena el Token
                    SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
                    SharedPreferences.Editor myEditor = myPreferences.edit();
                    //Se Guarda el Token Obtenido
                    myEditor.putString("Token", response.body().toString());
                    myEditor.commit();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplication().getBaseContext(),"Error " + t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }



}
