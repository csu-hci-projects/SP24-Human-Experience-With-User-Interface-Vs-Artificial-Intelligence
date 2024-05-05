import React, { useState, useEffect } from 'react';
import './Items.css';
import lean from './img/lean.jpg';
import jacked from './img/jacked.jpg';
import swole from './img/swole.jpg';



export default function BodyType({ updateDesiredWeight } ) {

    const [activeButton, setActiveButton] = useState(parseInt(localStorage.getItem('bodytype'), 10));
    const [desiredWeight, setWeight] = useState(localStorage.getItem('desired'));
    const [valid, setValid] = useState(false);

    const handleClick = (buttonID) => {
        setActiveButton(buttonID);
        localStorage.setItem('bodytype', buttonID);
    }

    const check = (event) => {
        console.log(event.target.checked);
        console.log(event.target.id);
    }

    const handleWeightChange = (event) => {
        const input = event.target.value;
        const newInput = parseInt(input);

        if (!isNaN(newInput) && newInput >= 80 && newInput <= 300) {
            setWeight(newInput);
            setValid(true);
            updateDesiredWeight(newInput);
            localStorage.setItem('desired', event.target.value);
        } else {
            setWeight(input);
            setValid(false);
            updateDesiredWeight("");
            localStorage.setItem('desired', event.target.value);
        }
    }

    useEffect(() => {
        const storedWeight = localStorage.getItem('desired');
        if (storedWeight) {
          const parsedWeight = parseInt(storedWeight);
          if (!isNaN(parsedWeight) && parsedWeight >= 80 && parsedWeight <= 300) {
            setValid(true);
          } else {
            setValid(false);
          }
        }
      }, []);

    return (
        <div className="checkbox">
            <div className="label">
                        <label>Ectomorph</label>
                        <button className={`bodytype ${activeButton === 1 ? 'blue-button' : ''}`}
                        onClick={() => handleClick(1)}>
                            <img src={lean} alt="lean" className="bodytype"></img>
                        </button>
                    </div>
                    <div className="label">
                        <label>Mesomorph</label>
                        <button className={`bodytype ${activeButton === 2 ? 'blue-button' : ''}`}
                        onClick={() => handleClick(2)}>
                            <img src={jacked} alt="lean" className="bodytype"></img>
                        </button>
                    </div>
                    <div className="label">
                        <label>Endomorph</label>
                        <button className={`bodytype ${activeButton === 3 ? 'blue-button' : ''}`}
                        onClick={() => handleClick(3)}>
                            <img src={swole} alt="lean" className="bodytype"></img>
                        </button>
                    </div>

                    <div>
                    <div className="desc">
                        <div>
                        <label className="desc">Desired Weight (lbs):</label>
                        </div>
                        <input
                        className="textbox"
                        placeholder="Enter weight"
                        type="text"
                        value={desiredWeight}
                        onChange={handleWeightChange}
                        />
                        {!valid ? (
                            <i className="fas fa-x fa-l red"></i>
                        ) : (
                            <i className="fas fa-check fa-xl green"></i>
                        )}
                        
                        </div>
                        <br></br><br></br><br></br><br></br><br></br>
                        <div>
                            <label>Lactose Intolerant</label>
                        </div>
                        <input type="checkbox" id="lactose" onClick={check} className="box" />
                        <br></br> <br></br> <br></br>
                        <div>
                            <label>Gluten Free</label>
                        </div>
                        <input type="checkbox" id="gluten" onClick={check} className="box" />
                        <br></br> <br></br> <br></br>
                        <div>
                            <label>Nut Free</label>
                        </div>
                        <input type="checkbox" id="nut" onClick={check} className="box" />
                    </div>
        </div>
    )
}