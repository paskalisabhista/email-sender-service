package ui.ac.id.law.service;


import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ui.ac.id.law.model.MessageEntity;
import ui.ac.id.law.repository.MessageEntityRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Service
public class EmailSenderService{

    @Autowired
    MessageEntityRepository messageEntityRepository;

    @Value("${app.apikey}")
    private String apiKey;

    @Value("${app.sender.email}")
    private String senderEmail;

    @Async
    public CompletableFuture<Integer> handleSendToEmail(MultipartFile file) throws IOException {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        File convertedFile = toFile(file);
        List<String> destinations = decodeDestination(convertedFile);
        String subject = decodeSubject(convertedFile);
        String message = decodeMessage(convertedFile);

        int count = 0;
        for (String dest: destinations) {
            try{
                sendTextMail(dest, subject, message);
                count++;

                Runnable saveToDB = () -> {
                    messageEntityRepository.save(new MessageEntity(dest, subject, message));
                };
                CompletableFuture<Void> asyncTask = CompletableFuture.runAsync(saveToDB);

            }catch(IOException e){
                // skip
            }

        }
        future.complete(count);
        return future;
    }

    public File toFile(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        File convertedFile = new File(file.getOriginalFilename());
        Files.write(convertedFile.toPath(), bytes);
        return convertedFile;
    }

    public List<String> decodeDestination(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        String string = scanner.nextLine();
        return Arrays.stream(string.split(" ")).toList();
    }

    public String decodeSubject(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        scanner.nextLine(); // dest
        return scanner.nextLine();
    }

    public String decodeMessage(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        scanner.nextLine(); // dest
        scanner.nextLine(); // subject
        StringBuilder builder = new StringBuilder();
        while(scanner.hasNext()){
            builder.append(scanner.nextLine());
            builder.append("\n");   // enter
        }
        return builder.toString();
    }

    public void sendTextMail(String dest, String subject, String message) throws IOException{
        Email from = new Email(senderEmail);
        Email to = new Email(dest);
        Content content = new Content("text/plain", message);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }
    }


}
