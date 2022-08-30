package com.rafaelhosaka.shareme.email;

import com.rafaelhosaka.shareme.user.LanguagePreference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmailBuilder {
    private String language;
    public EmailBuilder(String languagePreference){
        this.language = languagePreference;
    }

    public final String containerStyles = "max-width:600px;" +
            "margin:0 auto;"+
            "color:black;";

    public final String btnStyles = "display:inline-block;"+
            "border-radius: 30px;" +
            "padding: 1rem;" +
            "font-weight: 700;" +
            "border: 0;" +
            "background: #02690b;"+
            "color: white;"+
            "text-decoration:none;"+
            "padding: 20px;";

    public final String footer =  "<hr style=\"border-top-width:1px;border-top-color:#c5c5c5;margin:8px 0 48px;border-style:solid none none;\">"+
            "<div align=\"center\">"+
            "Rafael Hideki Hosaka © 2022 ShareMe"+
            "</div>";

    public final String unique =  "<span style=\"opacity:0\">"+ LocalDateTime.now()+"</span>";

    public List<String> verificationEmail(String url){
        List<String> subjectAndBody = new ArrayList();
        String subject = EmailTranslation.verificationEmailSubject(language);
        String body =
                "<div style=\"margin:0;padding:0;\">"+
                    "<div style=\""+containerStyles+"\">" +
                        "<div style=\"background:#f1f2f2;padding:20px;\" align=\"center\">"+
                            "<img style=\"width:200px;height:75px;\" src=\"cid:logo\">"+
                        "</div>"+
                        "<div style=\"margin-bottom:24px\" align=\"center\">"+
                            "<h1 style=\"margin:24px\">"+EmailTranslation.verificationEmailHeader(language)+"</h1>"+
                            "<p>"+EmailTranslation.verificationEmailBody(language)+"</p>"+
                            "<div style=\"margin-top:24px\" align=\"center\">"+
                                "<a href=\""+url+"\" style=\"" + btnStyles + "\">"+EmailTranslation.verificationEmailButton(language)+"</a>" +
                            "</div>"+
                        "</div>"+footer+
                    "</div>"+
                    unique+
                "</div>";

        subjectAndBody.add(subject);
        subjectAndBody.add(body);
        return subjectAndBody;
    }

    public List<String> passwordRecoveryEmail(String url, String to){
        List<String> subjectAndBody = new ArrayList();
        String subject = EmailTranslation.passwordRecoveryEmailSubject(language);
        String body = "<div style=\"margin:0;padding:0;\">"+
                    "<div style=\""+containerStyles+"\">" +
                        "<div style=\"background:#f1f2f2;padding:20px;\" align=\"center\">"+
                            "<img style=\"width:200px;height:75px;\" src=\"cid:logo\">"+
                        "</div>"+
                        "<div style=\"margin-bottom:24px\" align=\"center\">"+
                            "<p>"+EmailTranslation.passwordRecoveryEmailFirstParagraph(language,to)+"</p>"+
                            "<p>"+EmailTranslation.passwordRecoveryEmailSecondParagraph(language)+"</p>"+
                            "<div style=\"margin-top:24px\" align=\"center\">"+
                                "<a href=\""+url+"\" style=\"" + btnStyles + "\">"+EmailTranslation.passwordRecoveryEmailButton(language)+"</a>" +
                            "</div>"+
                        "</div>"+
                        footer+
                    "</div>"+
                    unique+
                "</div>";
        subjectAndBody.add(subject);
        subjectAndBody.add(body);
        return subjectAndBody;
    }

    public List<String> passwordChangedEmail(){
        List<String> subjectAndBody = new ArrayList();
        String subject = EmailTranslation.passwordChangedEmailSubject(language);
        String body = "<div style=\"margin:0;padding:0;\">"+
                    "<div style=\""+containerStyles+"\">" +
                        "<div style=\"background:#f1f2f2;padding:20px;\" align=\"center\">"+
                            "<img style=\"width:200px;height:75px;\" src=\"cid:logo\">"+
                        "</div>"+
                        "<div style=\"margin-bottom:24px\" align=\"center\">"+
                            "<h1 style=\"margin:24px\">"+EmailTranslation.passwordChangedEmailHeader(language)+"</h1>"+
                            "<p>"+EmailTranslation.passwordChangedEmailBody(language)+"</p>"+
                        "</div>"+
                        footer+
                    "</div>"+
                    unique+
                "</div>";
        subjectAndBody.add(subject);
        subjectAndBody.add(body);
        return subjectAndBody;
    }
}
