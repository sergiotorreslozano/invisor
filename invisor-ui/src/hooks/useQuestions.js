// src/hooks/useQuestions.js
import { useState, useEffect } from 'react';
import axios from 'axios';

export const useQuestions = (userId, folder) => {
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Read the base URL from the environment variable
  const baseUrl = process.env.REACT_APP_API_BASE_URL;

  useEffect(() => {
    if (!folder) return;

    const fetchQuestions = async () => {
      setLoading(true);
      try {
        const response = await axios.get(
          `${baseUrl}/api/users/${userId}/folders/${folder}/questions`
        );
        setQuestions(response.data);
        setError(null);
      } catch (err) {
        setError('Failed to load questions');
      } finally {
        setLoading(false);
      }
    };

    fetchQuestions();
  }, [userId, folder, baseUrl]);

  return { questions, loading, error };
};
