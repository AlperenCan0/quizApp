package com.canalperen.quizapp.service;

import com.canalperen.quizapp.dao.QuestionDao;
import com.canalperen.quizapp.dao.QuizDao;
import com.canalperen.quizapp.model.Question;
import com.canalperen.quizapp.model.QuestionWrapper;
import com.canalperen.quizapp.model.Quiz;
import com.canalperen.quizapp.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    private QuizDao quizDao;
    private QuestionDao questionDao;

    public QuizService(QuizDao quizDao, QuestionDao questionDao) {
        this.quizDao = quizDao;
        this.questionDao = questionDao;
    }

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Question> questionsForDB = quiz.get().getQuestions();

        List<QuestionWrapper> questionsForUser = new ArrayList<>();

        for (Question q : questionsForDB) {
            QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
            questionsForUser.add(qw);
        }

        return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Question> questions = quiz.get().getQuestions();
        int rightAnswer = 0;
        int questionIndex = 0;

        for (Response response : responses) {
            if (response.getResponse().equals(questions.get(questionIndex).getCorrectAnswer())) {
                rightAnswer++;
            }
            questionIndex++;
        }
        return new ResponseEntity<>(rightAnswer, HttpStatus.OK);
    }
}
