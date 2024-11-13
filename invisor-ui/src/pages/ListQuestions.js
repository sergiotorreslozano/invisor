// src/pages/ListQuestions.js
import React, { useState } from 'react';
import { useQuestions } from '../hooks/useQuestions';
import FolderSelector from '../components/FolderSelector';
import QuestionTable from '../components/QuestionTable';

const ListQuestions = () => {
  const userId = 'cd0543c0-c619-4aa4-8eb6-e1e721a4cc6a'; // Replace with actual user ID if dynamic
  const [folder, setFolder] = useState('');

  const { questions, loading, error } = useQuestions(userId, folder);

  const handleFolderChange = (newFolder) => {
    setFolder(newFolder);
  };

  return (
    <div>
      <h2>List Questions</h2>
      <FolderSelector selectedFolder={folder} onFolderChange={handleFolderChange} />
      {loading && <p>Loading questions...</p>}
      {error && <p>{error}</p>}
      {!loading && !error && <QuestionTable questions={questions} />}
    </div>
  );
};

export default ListQuestions;
