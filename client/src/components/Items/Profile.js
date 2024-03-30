import React from 'react';
import './Items.css';

export default function Profile({image, name, text, role}) {
    return (
        <div>
            <div className="desc-name">{name}</div>
            <label className="desc-role">{role}</label>
            <div className="picture-frame">
                <img src={image} alt="Gabel" className="profile-pic"/>
                <div className="desc-profile">
                    <label className="desc-font">{text}</label>
                </div>
            </div>
        </div>
    )
}