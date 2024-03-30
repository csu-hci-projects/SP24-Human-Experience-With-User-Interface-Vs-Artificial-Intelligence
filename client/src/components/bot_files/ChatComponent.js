import React, { useState, useRef, useEffect } from 'react';
import axios from 'axios';
import './ChatComponent.css'; // Import CSS file for styling
import { IoIosSend } from "react-icons/io";

function ChatComponent() {
    const [userInput, setUserInput] = useState('');
    const [messages, setMessages] = useState([
        { text: 'Welcome to SmartEatz! You can ask me questions and I will do my best to answer them! You can also type "input mode" for me to generate you a meal plan!', sender: 'bot' }
    ]);

    const messagesEndRef = useRef(null);

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
                }, 1000);
            })
            .catch(error => {
                console.error("API Request Error:", error);
            });
    };
    
    

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

    return (
        <div className="chat-container">
            <div className="message-container">
                {messages.map((message, index) => (
                    <div key={index} className={`message ${message.sender}`}>
                        {message.text}
                    </div>
                ))}
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