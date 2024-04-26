import React, { useState, useRef, useEffect } from 'react';
import axios from 'axios';
import './ChatComponent.css'; // Import CSS file for styling
import { IoIosSend } from "react-icons/io";

import lean from '../Items/img/lean.jpg';
import swole from '../Items/img/swole.jpg';
import jacked from '../Items/img/jacked.jpg';

function ChatComponent() {
    const [userInput, setUserInput] = useState('');
    const [messages, setMessages] = useState([
        { text: 'Welcome to SmartEatz! You can ask me questions and I will do my best to answer them! You can also type "input mode" for me to generate you a meal plan!', sender: 'bot' }
    ]);
    const [showImages, setShowImages] = useState(false);

    const messagesEndRef = useRef(null);

    const [mealData, setMealData] = useState(null);
    const [totalCalories, setTotalCalories] = useState(0);
    const [totalProtein, setTotalProtein] = useState(0);
    const [totalCarbs, setTotalCarbs] = useState(0);
    const [totalFat, setTotalFat] = useState(0);

    const sendApiRequest = (input) => {
        axios.post('http://localhost:3000/chatbot', { input })
            .then(response => {
                const newBotResponse = response.data;
                const newMessages = [...messages, { text: input, sender: 'user' }]; // Include user's input first
                setMessages(newMessages);
                setTimeout(() => {
                    const botResponseText = typeof newBotResponse === 'object' ? newBotResponse.input : newBotResponse;
                    const updatedMessages = [...newMessages, { text: botResponseText, sender: 'bot' }];
                    setMessages(updatedMessages);
                    setShowImages(botResponseText.includes("Final question! Which body type best resembles yours? (Just click the image that best fits)."));
                    if (botResponseText.includes("Alright, all done! Generating your meal plan now...")) {
                        
                            axios.post('http://localhost:3000/api')
                            .then(response => {
                                const mealData = response.data;
                                setMealData(mealData);
                                findTotals();
                              })
                              .catch(error => {
                                console.error("API Request Error:", error);
                              })

                    }
                }, 1000);
            })
            .catch(error => {
                console.error("API Request Error:", error);
            });
    };

    function findTotals() {
            console.log("here")
            let cals = 0;
            let prot = 0;
            let carb = 0;
            let fat = 0;
            for (let i  = 0; i < mealData.breakfast.Calories.length; i++) {
              cals += mealData.breakfast.Calories[i];
              prot += mealData.breakfast.Protein[i];
              carb += mealData.breakfast.Carbs[i];
              fat += mealData.breakfast.Fat[i];
            }
            for (let i  = 0; i < mealData.lunch.Calories.length; i++) {
              cals += mealData.lunch.Calories[i];
              prot += mealData.lunch.Protein[i];
              carb += mealData.lunch.Carbs[i];
              fat += mealData.lunch.Fat[i];
            }
            for (let i  = 0; i < mealData.dinner.Calories.length; i++) {
              cals += mealData.dinner.Calories[i];
              prot += mealData.dinner.Protein[i];
              carb += mealData.dinner.Carbs[i];
              fat += mealData.dinner.Fat[i];
            }
            setTotalCalories(cals);
            setTotalProtein(Math.round(prot));
            setTotalCarbs(Math.round(carb));
            setTotalFat(Math.round(fat));
          }
    
    const handleInputChange = (event) => {
        setUserInput(event.target.value);
    };

    const handleSubmit = () => {
        sendMessage(userInput); // Pass userInput to sendMessage function
    };

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            sendMessage(userInput); // Pass userInput to sendMessage function
        }
    };

    const sendMessage = (message) => { // Accept message as parameter
        if (!message.trim()) return; // Don't send empty messages
        const newUserMessage = { text: message, sender: 'user' }; // Use message parameter
        setMessages([...messages, newUserMessage]);
        setUserInput('');
        sendApiRequest(message); // Send the actual message
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    const handleImageClick = (number) => {
        setUserInput(number);
        sendMessage(number); // Send the selected number as a message
    };

    return (
        <div className="chat-container">
            <div className="message-container">
                {messages.map((message, index) => (
                    <div key={index} className={`message ${message.sender}`}>
                        {message.text}
                    </div>
                ))}
                {showImages && (
                    <div>
                        <div className="bodyNumbers">
                            <div>
                                <p onClick={() => handleImageClick('1')}>Skinny</p>
                                <img src={lean} alt="lean" className="bodyImage" onClick={() => handleImageClick('1')} />
                            </div>
                            <div>
                                <p onClick={() => handleImageClick('2')}>Lean</p>
                                <img src={jacked} alt="jacked" className="bodyImage" onClick={() => handleImageClick('2')} />
                            </div>
                            <div>
                                <p onClick={() => handleImageClick('3')}>Buff</p>
                                <img src={swole} alt="swole" className="bodyImage" onClick={() => handleImageClick('3')} />
                            </div>
                        </div>
                    </div>
                )}
                <div ref={messagesEndRef} />
            </div>
            <div className="input-container">
                <input
                    className="textbox"
                    placeholder="Type a message..."
                    type="text"
                    value={userInput}
                    onChange={handleInputChange}
                    onKeyDown={handleKeyDown}
                />
                <button className="chat-button" onClick={handleSubmit}>
                    <IoIosSend />
                </button>
            </div>
        </div>
    );
}

export default ChatComponent;
