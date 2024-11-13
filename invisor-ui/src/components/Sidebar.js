// src/components/Sidebar.js
import React from 'react';
import { Link } from 'react-router-dom';

const Sidebar = () => {
  return (
    <nav className="sidebar">
      <ul>
        <li><Link to="/">List Questions</Link></li>
        <li><Link to="/make-questions">Make Questions</Link></li>
        <li><Link to="/disclaimer">Disclaimer</Link></li>
      </ul>
    </nav>
  );
};

export default Sidebar;
