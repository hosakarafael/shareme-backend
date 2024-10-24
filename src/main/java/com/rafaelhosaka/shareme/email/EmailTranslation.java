package com.rafaelhosaka.shareme.email;

import com.rafaelhosaka.shareme.user.LanguagePreference;

public class EmailTranslation {
    public static String verificationEmailSubject(String language){
        switch (language){
            case "ja":
                return "ShareMeアカウント確認用メール";
            case "pt":
                return "Email de verificação da conta ShareMe";
            default:
                return "ShareMe Account Verification";
        }
    }

    public static String verificationEmailHeader(String language){
        switch (language){
            case "ja":
                return "ShareMeへようこそ！";
            case "pt":
                return "Bem vindo à ShareMe!";
            default:
                return "Welcome to ShareMe!";
        }
    }

    public static String verificationEmailBody(String language){
        switch (language){
            case "ja":
                return "このメールは先ほどShareMeにアカウント登録をしたのでお送りしております。";
            case "pt":
                return "Você está recebendo esta mensagem porque se inscreveu em uma conta no ShareMe. (Se você não se inscreveu, pode ignorar este e-mail).";
            default:
                return "You're receiving this message because you signed up for an account on ShareMe. (If you didn't sign up, you can ignore this email.)";
        }
    }

    public static String verificationEmailButton(String language){
        switch (language){
            case "ja":
                return "メールアドレスを確認するにはここをクリックしてください";
            case "pt":
                return "Clique aqui para ativar sua conta";
            default:
                return "Click here to activate your account";
        }
    }

    public static String passwordRecoveryEmailSubject(String language){
        switch (language){
            case "ja":
                return "ShareMeパスワード再設定";
            case "pt":
                return "Recuperação de senha do ShareMe";
            default:
                return "ShareMe Password Recovery";
        }
    }

    public static String passwordRecoveryEmailFirstParagraph(String language, String to){
        switch (language){
            case "ja":
                return "メールアドレス"+to +"のパスワード再設定の申し込みがありました";
            case "pt":
                return "Recebemos uma solicitação para redefinir a senha da sua conta "+ to;
            default:
                return "We received a request to reset your password for your ShareMe account "+to;
        }
    }

    public static String passwordRecoveryEmailSecondParagraph(String language){
        switch (language){
            case "ja":
                return "パスワードを再設定するには下のボタンをクリックしてください";
            case "pt":
                return "Clique no botão abaixo para definir uma nova senha";
            default:
                return "Please click on the button below to set a new password.";
        }
    }

    public static String passwordRecoveryEmailButton(String language){
        switch (language){
            case "ja":
                return "パスワードを再設定する";
            case "pt":
                return "Definir nova senha";
            default:
                return "Set new password";
        }
    }

    public static String passwordChangedEmailSubject(String language){
        switch (language){
            case "ja":
                return "パスワードを変更いたしました";
            case "pt":
                return "Você alterou sua senha";
            default:
                return "You changed your password";
        }
    }


    public static String passwordChangedEmailHeader(String language){
        switch (language){
            case "ja":
                return "ShareMeアカウントのパスワードを変更いたしました";
            case "pt":
                return "A senha de sua conta ShareMe foi alterada";
            default:
                return "Your ShareMe account's password changed";
        }
    }

    public static String passwordChangedEmailBody(String language){
        switch (language){
            case "ja":
                return "もし心当たりがない場合はすぐにお問い合わせをお願いいたします";
            case "pt":
                return "Se não tiver alterado sua senha, entre em contato conosco imediatamente";
            default:
                return "If you didn't change your password, please contact us right away";
        }
    }
}
