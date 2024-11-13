// src/App.js
import './App.css';
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Sidebar from './components/Sidebar';
import Footer from './components/Footer';
import ListQuestions from './pages/ListQuestions';
import MakeQuestions from './pages/MakeQuestions';
import Disclaimer from './pages/Disclaimer';

const App = () => {
  return (
    <Router>
      <div className="app">
        <Header />
        <div className="main-content">
          <Sidebar />
          <div className="content">
            <Routes>
              <Route path="/" element={<ListQuestions />} />
              <Route path="/make-questions" element={<MakeQuestions />} />
              <Route path="/disclaimer" element={<Disclaimer />} />
            </Routes>
          </div>
        </div>
        <Footer />
      </div>
    </Router>
  );
};

export default App;
