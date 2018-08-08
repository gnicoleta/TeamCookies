package group.msg.jsf_beans;

import lombok.Data;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean
@ViewScoped
@Data
public class MessageTableController implements Serializable {

    private static final long serialVersionUID = 20111020L;

    private List<Message> messages;
    private List<Message> filteredMessages;
    private String newSubject = "my subject";
    private String newText = "my text";
    private final static String[] states;
    private Date currentDate = new Date();
    private boolean checkValue;

    static {
        states = new String[10];
        for (int i = 0; i < 10; i++) {
            states[i] = "state" + i;
        }
    }

    public MessageTableController() {
        if (messages == null) {
            messages = new ArrayList<Message>();

            for (int i = 0; i < 10; i++) {
                Message message = new Message();
                message.setSubject("subject " + i);
                message.setText("text " + i);
                message.setTextLength(((i * 10) + 10) + "");
                message.setCountry("country" + i);
                message.setState("state" + i);
                message.setDeliveryStatus("successfull");
                messages.add(message);
            }
        }
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(final List<Message> messages) {
        this.messages = messages;
    }

    public List<Message> getFilteredMessages() {
        return filteredMessages;
    }

    public void setFilteredMessages(List<Message> filteredMessages) {
        this.filteredMessages = filteredMessages;
    }

    public String getNewSubject() {
        return newSubject;
    }

    public void setNewSubject(String newSubject) {
        this.newSubject = newSubject;
    }

    public String getNewText() {
        return newText;
    }

    public void setNewText(String newText) {
        this.newText = newText;
    }

    public String[] getStates() {
        return states;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public boolean getCheckValue(){
        return checkValue;
    }

    public void setCheckValue(boolean checkValue) {
        this.checkValue = checkValue;
    }
    public String addMessage() {
        doSomething();

        Message message = new Message();
        message.setSubject(newSubject);
        message.setText(newText);
        messages.add(0, message);

        newSubject = "";
        newText = "";

        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New message has been added successfully", null));

        return null;
    }

    public void doSomething() {
        try {
            // simulate a long running request
            Thread.sleep(1500);
        } catch (Exception e) {
            // ignore
        }
    }

    public class Message implements Serializable {

        private String subject;
        private String text;
        private long time;
        private String textLength;
        private String country;
        private String state;
        private String deliveryStatus;

        public Message() {
            time = System.currentTimeMillis() + (long) (Math.random() * 10);
            textLength = Math.random() * 10 + "";
        }

        public final String getSubject() {
            return subject;
        }

        public final void setSubject(String subject) {
            this.subject = subject;
        }

        public final String getText() {
            return text;
        }

        public final void setText(String text) {
            this.text = text;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getTextLength() {
            return textLength;
        }

        public void setTextLength(String textLength) {
            this.textLength = textLength;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getDeliveryStatus() {
            return deliveryStatus;
        }

        public void setDeliveryStatus(String deliveryStatus) {
            this.deliveryStatus = deliveryStatus;
        }
    }
}