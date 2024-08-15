package com.codegym.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TinderBoltApp extends SimpleTelegramBot {

    public static final String TELEGRAM_BOT_TOKEN = "AQUI VA EL token DEL BOT TELEGRAM"; //TODO: añadir el token del bot entre comillas
    public static final String OPEN_AI_TOKEN = "AQUI VA TOKEN ChatGPT"; //TODO: añadir el token de ChatGPT entre comillas

    private ChatGPTService chatGpt = new ChatGPTService(OPEN_AI_TOKEN);
    private DialogMode mode;

    public TinderBoltApp() {
        super(TELEGRAM_BOT_TOKEN);
    }

    //TODO: escribiremos la funcionalidad principal del bot aquí
    public void startCommand(){

        mode = DialogMode.MAIN;
        String text = loadMessage("main");
        sendPhotoMessage("main");
        sendTextMessage(text);

        showMainMenu(

                "start", "menú principal del bot",
                "profile", "generación de perfil de Tinder \uD83D\uDE0E",
                "opener", "mensaje para iniciar conversación \uD83E\uDD70",
                "message", "correspondencia en su nombre \uD83D\uDE08",
                "date", "correspondencia con celebridades \uD83D\uDD25",
                "gpt", "hacer una pregunta a chat GPT \uD83E\uDDE0"
        );
    }

    //nos  coloca en modo chatGPT
    public void gtpCommand(){

        mode = DialogMode.GPT;

        String text = loadMessage("gpt");
        //mostrar imagen
        sendPhotoMessage("gpt");
        sendTextMessage(text);
    }
    //Metodo que llama a chatGPT y hace la consulta
    public void gptDialog(){

        String text = getMessageText();
        String prompt = loadPrompt("gpt");
        String answer = chatGpt.sendMessage(prompt,text);
        sendTextMessage(answer);
    }

    public void mensaje(){

        if (mode == DialogMode.GPT){
            gptDialog();

        }else {

            String respuesta = getMessageText();

            sendTextMessage("*Hello*");
            sendTextMessage("_How are you?_");
            sendTextMessage("you wrote: " + respuesta);
            //mostrar imagenenes
            sendPhotoMessage("avatar_main");
            //Botones
            sendTextButtonsMessage("Launch process",
                    "start", "Empezar",
                    "stop", "Detener");
        }
    }

    public void helloButton(){

        String key = getButtonKey();

        if (key.equals("start")){
            sendTextMessage("Has dado click en empezar compita");
        }else {
            sendTextMessage("Poruqe cancelas mi cuate?");
        }

    }
    @Override
    public void onInitialize() {
        //TODO: y un poco más aquí :)
        addCommandHandler("start", this::startCommand);
        addCommandHandler("gpt", this::gtpCommand);
        addMessageHandler(this::mensaje);
        addButtonHandler("^.*",this::helloButton);
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }
}
