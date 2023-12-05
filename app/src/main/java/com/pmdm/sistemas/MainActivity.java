package com.pmdm.sistemas;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;
    private Spinner spinner;
    private Button showInfoButton;
    private TextView infoTextView;

    // Mapa que asocia modelos de dispositivos con detalles
    private Map<String, DeviceDetails> deviceDetailsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        spinner = findViewById(R.id.spinner);
        showInfoButton = findViewById(R.id.showInfoButton);
        infoTextView = findViewById(R.id.infoTextView);

        // Configurar el menú contextual
        View rootView = findViewById(android.R.id.content);
        registerForContextMenu(rootView);


        // Datos para el AutoCompleteTextView y el Spinner
        String[] devices = {"Samsung S23", "iPhone 14", "Lumia 640"};
        String[] operatingSystems = {"Sistema operativo", "Android OS", "iOS", "Windows"};

        // Configurar adaptadores
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, devices);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, operatingSystems);
        spinner.setAdapter(spinnerAdapter);

        // Inicializar el mapa de dispositivos y detalles
        initializeDeviceDetailsMap();

        // Configurar listener para AutoCompleteTextView
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el dispositivo seleccionado y su sistema operativo asociado
                String selectedDevice = autoCompleteAdapter.getItem(position);
                String associatedOs = deviceDetailsMap.get(selectedDevice).getOperatingSystem();

                // Buscar la posición del sistema operativo correspondiente y seleccionarlo en el Spinner
                int osPosition = getOSPosition(associatedOs, operatingSystems);
                spinner.setSelection(osPosition);
            }
        });

        // Configurar listener para el botón
        showInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar información del dispositivo seleccionado
                String selectedDevice = autoCompleteTextView.getText().toString();
                displayInfo(selectedDevice);
            }
        });
    }

    private void initializeDeviceDetailsMap() {
        // Inicializar el mapa con asociaciones de dispositivos y detalles
        deviceDetailsMap = new HashMap<>();
        // Detalles específicos para Samsung S23
        deviceDetailsMap.put("Samsung S23", new DeviceDetails("Android OS", "Exynos 990", "Octa-core", "Mali-G77 MP11"));
        // Detalles específicos para iPhone 14
        deviceDetailsMap.put("iPhone 14", new DeviceDetails("iOS", "Apple A16 Bionic", "Hexa-core", "Apple GPU"));
        // Detalles específicos para Lumia 640
        deviceDetailsMap.put("Lumia 640", new DeviceDetails("Windows", "Snapdragon 400", "Quad-core", "Adreno 305"));
    }

    private int getOSPosition(String os, String[] operatingSystems) {
        // Buscar la posición del sistema operativo correspondiente
        for (int i = 0; i < operatingSystems.length; i++) {
            if (operatingSystems[i].equals(os)) {
                return i;
            }
        }
        return 0; // Valor predeterminado
    }

    private void displayInfo(String selectedDevice) {
        // Mostrar información del dispositivo seleccionado
        DeviceDetails details = deviceDetailsMap.get(selectedDevice);

        // Construir el mensaje de información
        String info = "Información sobre " + selectedDevice + ":\n" +
                "Sistema Operativo: " + details.getOperatingSystem() + "\n" +
                "Procesador: " + details.getProcessor() + "\n" +
                "Núcleos: " + details.getNumberOfCores() + "\n" +
                "GPU: " + details.getGpu();

        // Mostrar la información en el TextView
        infoTextView.setText(info);
    }

    // Clase para almacenar detalles del dispositivo
    private static class DeviceDetails {
        private String operatingSystem;
        private String processor;
        private String numberOfCores;
        private String gpu;

        public DeviceDetails(String operatingSystem, String processor, String numberOfCores, String gpu) {
            this.operatingSystem = operatingSystem;
            this.processor = processor;
            this.numberOfCores = numberOfCores;
            this.gpu = gpu;
        }

        public String getOperatingSystem() {
            return operatingSystem;
        }

        public String getProcessor() {
            return processor;
        }

        public String getNumberOfCores() {
            return numberOfCores;
        }

        public String getGpu() {
            return gpu;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu); // Crea el menú desde el recurso de menú
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Maneja la opción seleccionada en el menú contextual
        if (item.getItemId() == R.id.menu_info) {
            showInfo();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void showInfo() {
        // Lógica para mostrar información adicional
        String additionalInfo = "No soy perezoso, estoy en modo ahorro de energía.";

        // Muestra la información en un Toast (puedes cambiar esto según tus necesidades)
        Toast.makeText(this, additionalInfo, Toast.LENGTH_SHORT).show();
    }
}
