package ui.ac.id.law.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import ui.ac.id.law.service.EmailSenderService;

import java.io.IOException;

@RestController
public class AppController {

    @Autowired
    private EmailSenderService emailSenderService;

//    @PostMapping("/send-to-email")
//    public ResponseEntity<String> sendToEmail(@RequestParam("file") MultipartFile file) {
//        int x;
//        try{
//            x = emailSenderService.handleSendToEmail(file);
//        }catch (IOException e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurs!");
//        }
//        return ResponseEntity.ok().body("Message has been sent to " + x + " email(s)");
//    }

    @PostMapping("/send-to-email")
    public DeferredResult<ResponseEntity<String>> sendToEmailAsync(@RequestParam("file") MultipartFile file) {
        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
        try{
            emailSenderService.handleSendToEmail(file)
                    .whenComplete((result, throwable) -> {
                        deferredResult.setResult(ResponseEntity.ok().body("Message has been sent to " + result + " email(s)"));
                    });
        }catch (IOException e){
            deferredResult.setResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurs!"));
        }
        return deferredResult;
    }
}
