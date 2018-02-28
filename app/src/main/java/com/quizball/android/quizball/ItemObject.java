package com.quizball.android.quizball;

/**
 * Created by dozie on 2017-08-30.
 */

public class ItemObject {
    private String _questions;
    private String _firstAns;
    private String _secondAns;
    private String _thirdAns;
    private String _fourthAns;
    private String _fifthAns;
    private int _answer;
    private int _ifAnswered;
    private int _answerState;

    public ItemObject(String questions, String firstAns, String secondAns, String thirdAns, String fourthAns, String fifthAns, int answer, int ifAnswered, int answerState)
    {
        this._questions = questions;
        this._firstAns = firstAns;
        this._secondAns = secondAns;
        this._thirdAns = thirdAns;
        this._fourthAns = fourthAns;
        this._fifthAns = fifthAns;
        this._answer = answer;
        this._ifAnswered = ifAnswered;
        this._answerState = answerState;
    }

    public String getQuestions()
    {
        return _questions;
    }

    public void setQuestions(String questions)
    {
        this._questions = questions;
    }

    public String get_firstAns()
    {
        return _firstAns;
    }

    public void set_firstAns(String firstAns)
    {
        this._firstAns = firstAns;
    }

    public String get_secondAns()
    {
        return _secondAns;
    }

    public void set_secondAns(String secondAns)
    {
        this._secondAns = secondAns;
    }

    public String get_thirdAns()
    {
        return _thirdAns;
    }

    public void set_thirdAns(String thirdAns)
    {
        this._thirdAns = thirdAns;
    }

    public String get_fourthAns()
    {
        return _fourthAns;
    }

    public void set_fourthAns(String fourthAns)
    {
        this._fourthAns = fourthAns;
    }

    public String get_fifthAns()
    {
        return _fifthAns;
    }

    public void set_fifthAns(String fifthAns)
    {
        this._fifthAns = fifthAns;
    }

    public int get_answer()
    {
        return _answer;
    }

    public void set_answer(int answer)
    {
        this._answer = answer;
    }

    public int get_ifAnswered()
    {
        return _ifAnswered;
    }

    public void set_ifAnswered(int ifAnswered)
    {
        this._ifAnswered = ifAnswered;
    }

    public int get_answerState()
    {
        return _answerState;
    }

    public void set_answerState(int answerState)
    {
        this._answerState = answerState;
    }
}
