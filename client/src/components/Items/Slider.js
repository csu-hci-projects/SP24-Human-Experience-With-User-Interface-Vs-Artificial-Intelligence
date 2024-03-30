import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import Hamburger from './Hamburger';

export default function Slider({text}) {

    const [isHamOpen, setIsHamOpen] = useState(false);

    const toggleHam = () => {
    setIsHamOpen(!isHamOpen);
  }

    return (
        <div>
            
            <header className="Slider">
                <button className="burger-button left" onClick={toggleHam}>
                        <Hamburger />
                </button>
            <div className="center">
            {text}
            </div>
            </header>
            <div className={`sliding-menu ${isHamOpen ? 'open' : ''}`}>
                <button className="burger-button-modal" onClick={toggleHam}>
                <Hamburger />
                </button>
                <Link to="/" className="Menu-buttons">
                <i className="fas fa-house fa-xs"> Home</i>
                </Link>
                <p />
                <Link to="/About" className="Menu-buttons">
                <i className="fas fa-users fa-xs"> About Us</i>
                </Link>
                <p />
                <Link to="/Chat" className="Menu-buttons">
                <i className="fas fa-message fa-xs"> Chat</i>
                </Link>
            </div>

        </div>
    )
}