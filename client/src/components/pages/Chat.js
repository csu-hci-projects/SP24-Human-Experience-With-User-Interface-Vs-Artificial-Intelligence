import React, { useState } from 'react';
import ChatComponent from '../bot_files/ChatComponent';
import Slider from '../Items/Slider';
import { FaRegMessage } from "react-icons/fa6";
import './Chat.css';

export default function Chat() {
    const [isChatOpen, setIsChatOpen] = useState(false);

    const toggleChat = () => {
        setIsChatOpen(!isChatOpen);
    };

    return (
        <div className="Pre-Start">
            <Slider text="Press the message box to get started!"/>
            <div className="Chat">
              <p className="chat-text">
                This is blank text currently. Here will be the description of the website and information about the team who developed it...
                <br /> <br /> <br />
                To start, you can press the chat box in the bottom right of your screen.
              </p>
                <div className="ChatIcon" onClick={toggleChat}><FaRegMessage /></div>
                {isChatOpen && <ChatComponent />}
            </div>
        </div>
    );
}
