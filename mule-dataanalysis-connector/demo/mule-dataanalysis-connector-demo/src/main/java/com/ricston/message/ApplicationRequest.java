/**
 * (c) 2003-2014 Ricston, Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.ricston.message;

public class ApplicationRequest
{
    private String name;
    private String surname;
    private int age;
    private String comments;

    public ApplicationRequest()
    {
        super();
    }

    public ApplicationRequest(String name, String surname, int age, String comments)
    {
        super();
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.comments = comments;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSurname()
    {
        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }

}
