import React, { useState, useEffect } from 'react';
//import { Link } from 'react-router-dom';
import Slider from '../Items/Slider';
import BodyType from '../Items/BodyType';
import { Link } from 'react-router-dom';
import './Start.css';

export default function Start() {

    const [age, setAge] = useState(localStorage.getItem('age'));
    const [height, setHeight] = useState(localStorage.getItem('height'));
    const [weight, setWeight] = useState(localStorage.getItem('weight'));
    const [sex, setSex] = useState(localStorage.getItem('sex'));
    const [activity, setActivity] = useState(localStorage.getItem('activity'));
    const [valid, setValid] = useState(false);
    const [ready, setReady] = useState(false);
    const [body, setBody] = useState(null);
    const [desiredWeight, setDesiredWeight] = useState(localStorage.getItem('desired'));


    const handleAgeChange = (event) => {
        setAge(event.target.value);
        localStorage.setItem('age', event.target.value);
    };

    const handleHeightChange = (event) => {
        setHeight(event.target.value);
        localStorage.setItem('height', event.target.value);
    }

    const handleSexChange = (event) => {
        setSex(event.target.value);
        localStorage.setItem('sex', event.target.value);
    };

    const handleActivityChange = (event) => {
        setActivity(event.target.value);
        localStorage.setItem('activity', event.target.value);
    };

    const handleWeightChange = (event) => {
        const input = event.target.value;
        const newInput = parseInt(input);

        if (!isNaN(newInput) && newInput >= 80 && newInput <= 300) {
            setWeight(newInput);
            localStorage.setItem('weight', event.target.value);
            setValid(true);
        } else {
            setWeight(input);
            localStorage.setItem('weight', event.target.value);
            setValid(false);
        }
    }

    useEffect(() => {
        const storedWeight = localStorage.getItem('weight');
        setBody(localStorage.getItem('bodytype'));
        if (storedWeight) {
          const parsedWeight = parseInt(storedWeight);
          if (!isNaN(parsedWeight) && parsedWeight >= 80 && parsedWeight <= 300) {
            setValid(true);
          } else {
            setValid(false);
          }
        }
      }, []);

    const updateDesiredWeight = (num) => {
        setDesiredWeight(num);
    }

    const checkResults = (age, height, valid, desiredWeight, sex, activity, body) => {
        if (age && height && valid && desiredWeight && sex && activity && body) {
            setReady(true);
        } else {
            setReady(false);
        }
    }

    useEffect(() => {
        checkResults(age, height, valid, desiredWeight, sex, activity, body);
    }, [age, height, valid, desiredWeight, sex, activity, body]);

  const ageOptions = [];
  for (let age = 12; age <= 80; age++) {
    ageOptions.push(age);
  }

  const heightOptions = [];
    for (let i = 4; i <= 7; i++) {
        for (let j = 0; j <= 11; j++) {
            let temp = i + "' " + j + "\"";
            heightOptions.push(temp);
        }
    }

    return (
        <div class="Pre-Start">
            <Slider text="Please enter your information"/>
            <div>
                <div className="bottom">
                    {ready && (
                    <Link to='/MealPlan' className="next">
                        Continue
                        <br></br>
                    <i className="fas fa-arrow-right fa-xl"></i>
                    </Link>
                    )}
                </div>
                <div class="Start">
                    <div className="desc">
                        <label className="desc">Age:      </label>
                        <select value={age} onChange={handleAgeChange} className="dropdown">
                        <option value="">-- Select Age --</option>
                        {ageOptions.map((age) => (
                        <option> {age} </option>
                        ))}
                        </select>
                        {age && (
                            <label>
                            <i className="fas fa-check fa-xl green"></i>
                            </label>
                        )}
                    </div>

                    <div className="desc">
                        <label className="desc">Height:      </label>
                        <select value={height} onChange={handleHeightChange} className="dropdown">
                        <option value="">-- Select Height --</option>
                        {heightOptions.map((height) => (
                        <option> {height} </option>
                        ))}
                        </select>
                        {height && (
                            <label>
                            <i className="fas fa-check fa-xl green"></i>
                            </label>
                        )}
                    </div>

                    <div className="desc">
                        <label className="desc">Weight (lbs):      </label>
                        <input
                        className="textbox"
                        placeholder="Enter weight"
                        type="text"
                        value={weight}
                        onChange={handleWeightChange}
                        />
                        {!valid ? (
                            <i className="fas fa-x fa-l red"></i>
                        ) : (
                            <i className="fas fa-check fa-xl green"></i>
                        )}
                        
                    </div>

                    <div className="desc">
                        <label className="desc">Gender:      </label>
                        <select value={sex} onChange={handleSexChange} className="dropdown">
                        <option value="">-- Select Gender --</option>
                        <option>Male</option>
                        <option>Female</option>
                        </select>
                        {sex && (
                            <label>
                            <i className="fas fa-check fa-xl green"></i>
                            </label>
                        )}
                    </div>

                    <div className="desc">
                        <label className="desc">Activity Level:      </label>
                        <select value={activity} onChange={handleActivityChange} className="dropdown">
                        <option value="">-- Select Activity --</option>
                        <option>Not Active (1-2 Days/Week)</option>
                        <option>Active (3-4 Days/Week)</option>
                        <option>Extremely Active (5-7 Days/Week)</option>
                        </select>
                        {activity && (
                            <label>
                            <i className="fas fa-check fa-xl green"></i>
                            </label>
                        )}
                    </div>
                </div>

                <div>
                    <BodyType updateDesiredWeight={updateDesiredWeight} />
                </div>
            </div>
            <div className="bottom-filler">

            </div>
        </div>
    )
}