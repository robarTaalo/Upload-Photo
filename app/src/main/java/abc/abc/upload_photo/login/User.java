package abc.abc.upload_photo.login;


public class User {
    private String uuid, name, phone, email, photo, provider, myRequest;
    private boolean enabled;

    public User() {
    }

    public User(String uuid, String name, String phone, String email, String photo, String provider, String myRequest, boolean enabled) {
        this.uuid = uuid;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.photo = photo;
        this.provider = provider;
        this.myRequest = myRequest;
        this.enabled = enabled;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getMyRequest() {
        return myRequest;
    }

    public void setMyRequest(String myRequest) {
        this.myRequest = myRequest;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


}
