package com.rafaelhosaka.shareme.email;

import com.rafaelhosaka.shareme.user.LanguagePreference;

public class EmailTranslation {
    public static String verificationEmailSubject(String language){
        switch (language){
            case "ja":
                return "ShareMeアカウント確認用メール";
            default:
                return "ShareMe Account Verification";
        }
    }

    public static String verificationEmailHeader(String language){
        switch (language){
            case "ja":
                return "ShareMeへようこそ！";
            default:
                return "Welcome to ShareMe!";
        }
    }

    public static String verificationEmailBody(String language){
        switch (language){
            case "ja":
                return "このメールは先ほどShareMeにアカウント登録をしたのでお送りしております。";
            default:
                return "You're receiving this message because you signed up for an account on ShareMe. (If you didn't sign up, you can ignore this email.)";
        }
    }

    public static String verificationEmailButton(String language){
        switch (language){
            case "ja":
                return "メールアドレスを確認するにはここをクリックしてください";
            default:
                return "Click here to activate your account";
        }
    }

    public static String passwordRecoveryEmailSubject(String language){
        switch (language){
            case "ja":
                return "ShareMeパスワード再設定";
            default:
                return "ShareMe Password Recovery";
        }
    }

    public static String passwordRecoveryEmailFirstParagraph(String language, String to){
        switch (language){
            case "ja":
                return "メールアドレス"+to +"のパスワード再設定の申し込みがありました";
            default:
                return "We received a request to reset your password for your ShareMe account "+to;
        }
    }

    public static String passwordRecoveryEmailSecondParagraph(String language){
        switch (language){
            case "ja":
                return "パスワードを再設定するには下のボタンをクリックしてください";
            default:
                return "Please click on the button below to set a new password.";
        }
    }

    public static String passwordRecoveryEmailButton(String language){
        switch (language){
            case "ja":
                return "パスワードを再設定する";
            default:
                return "Set new password";
        }
    }

    public static String passwordChangedEmailSubject(String language){
        switch (language){
            case "ja":
                return "パスワードを変更いたしました";
            default:
                return "You changed your password";
        }
    }


    public static String passwordChangedEmailHeader(String language){
        switch (language){
            case "ja":
                return "ShareMeアカウントのパスワードを変更いたしました";
            default:
                return "Your ShareMe account's password changed";
        }
    }

    public static String passwordChangedEmailBody(String language){
        switch (language){
            case "ja":
                return "もし心当たりがない場合はすぐにお問い合わせをお願いいたします";
            default:
                return "If you didn't change your password, please contact us right away";
        }
    }
}
