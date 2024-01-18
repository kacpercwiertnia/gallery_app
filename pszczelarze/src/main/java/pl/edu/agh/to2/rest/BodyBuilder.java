package pl.edu.agh.to2.rest;

public class BodyBuilder{ //chcialem użyc wzorca adapter ale StringBuilder jest finalem :((
    private StringBuilder builder;

    public BodyBuilder(){
        builder = new StringBuilder();
    }

    public BodyBuilder openBody(){
        builder.append("{");
        return this;
    }

    public String closeAndGetBody(){
        return builder.append("}").toString();
    }

    public BodyBuilder addFirstKeyWithValue(String key, String value){
        builder.append(addQuotes(key))
                .append(": ")
                .append(addQuotes(value));

        return this;
    }

    public BodyBuilder addFirstKeyWithValue(String key, int value){
        builder.append(addQuotes(key))
                .append(": ")
                .append(value);

        return this;
    }

    public BodyBuilder addNextKey(String key, String value){
        builder.append(", ")
                .append(addQuotes(key))
                .append(": ")
                .append(addQuotes(value));
        return this;
    }

    public BodyBuilder addNextKey(String key, int value){
        builder.append(", ")
                .append(addQuotes(key))
                .append(": ")
                .append(value);
        return this;
    }

    private String addQuotes(String text){
        return "\"" + text + "\"";
    }
}
