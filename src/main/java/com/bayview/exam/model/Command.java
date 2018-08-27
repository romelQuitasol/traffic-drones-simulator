package com.bayview.exam.model;

import com.bayview.exam.enums.CommandType;

/**
 * Created by romelquitasol on 24/08/2018.
 */
public class Command {

  private String sender;
  private CommandType commandType;
  private Object data;

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public CommandType getCommandType() {
    return commandType;
  }

  public void setCommandType(CommandType commandType) {
    this.commandType = commandType;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("Data{");
    sb.append("sender='").append(sender).append('\'');
    sb.append(", commandName=").append(commandType);
    sb.append(", data=").append(data);
    sb.append('}');
    return sb.toString();
  }

  /**
   * Constructor with builder parameter
   */
  public Command(Builder builder) {
    this.sender = builder.sender;
    this.commandType = builder.commandType;
    this.data = builder.data;
  }

  public static class Builder {

    private String sender;
    private CommandType commandType;
    private Object data;

    public Builder sender(String sender) {
      this.sender = sender;
      return this;
    }

    public Builder commandType(CommandType commandType) {
      this.commandType = commandType;
      return this;
    }

    public Builder data(Object data) {
      this.data = data;
      return this;
    }

    public Command build() {
      return new Command(this);
    }
  }


}
