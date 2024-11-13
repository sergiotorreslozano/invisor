// src/components/FolderSelector.js
import React from 'react';

const FolderSelector = ({ selectedFolder, onFolderChange }) => {
  return (
    <select value={selectedFolder} onChange={(e) => onFolderChange(e.target.value)}>
      <option value="">Select folder</option>
      <option value="tep">Teleperformance</option>
      <option value="nfg">Next fifteen</option>
      <option value="paypal">Paypal</option>
    </select>
  );
};

export default FolderSelector;
