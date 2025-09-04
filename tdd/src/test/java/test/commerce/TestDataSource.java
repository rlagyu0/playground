package test.commerce;

public class TestDataSource {

    public static String[] invalidPasswords() {
        return new String[] {
            "pass123",
            null,
            "",
            "pass",
            "1234password",
            "password1234",
            "pass5678word"
        };
    };

    public static String[] invalidEmails() {
        return new String[] {
            "invalid-email",
            "invalid-email@",
            "invalid-email@test.",
            "invalid-email@.com",
            null
        };
    };
}
