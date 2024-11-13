// src/components/QuestionTable.js
import React from 'react';

const QuestionTable = ({ questions }) => {
  if (!questions.length) {
    return <p>No questions available.</p>;
  }

  return (
    <table>
      <thead>
        <tr>
          <th>UUID</th>
          <th>Question</th>
          <th>Answer</th>
          <th>Type</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>
        {questions.map((question) => (
          <tr key={question.uuid}>
            <td>{question.uuid}</td>
            <td>{question.question}</td>
            <td>{question.answer}</td>
            <td>{question.questionType}</td>
            <td>{question.questionStatus}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default QuestionTable;
