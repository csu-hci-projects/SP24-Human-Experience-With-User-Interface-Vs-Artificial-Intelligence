import React from 'react';
import './Items.css';

export default function Hamburger() {
    return (
        <div>
            <Bar></Bar>
            <Bar></Bar>
            <Bar></Bar>
        </div>
    )
}

function Bar() {
    return (
        <div class="Bar"></div>
    );
}