import React from 'react';
import './Start.css';
import Slider from '../Items/Slider';
import Profile from '../Items/Profile';

import Gabel from '../Items/img/Gabel.jpeg';
import Jack from '../Items/img/Jack.png';
import Tucker from '../Items/img/Tucker.jpg';
import Los from '../Items/img/Los.png';
import Cole from '../Items/img/Cole.png';
import Than from '../Items/img/Than.jpg';
//import nobody from '../Items/img/nobody.jpg';

export default function About() {
    return (
        <div class="Pre-Start">
            <Slider text="Meet the team!"/>
            <div className="About">
                <br></br><br></br><br></br>

                <div className="row">
                    <div>
                        <Profile image={Gabel} name="Jeremiah Gabel" role="Lead Developer" text="Hello, my name is Jeremiah Gabel, I'm the architect of this project. I'm currently a senior at CSU majoring in software engineering, I also work as a software developer for the USDA. I have a deep passion for maintaining a healthy lifestyle, and I believe this tool could be extremely beneficial to building a healthy diet." />
                    </div>
                    <div>
                        <Profile image={Jack} name="Jack Mullins" role="Coordinator" text="My name is Jack Mullins. I am a student from CSU who is studying Business Finance and Computer Information Systems. I joined the SmartEatz team to learn and assist with systems and User Interface of a website. I am excited to also learn how to eat healthier so I can live a happy healthy life!" />
                    </div>
                    <div>
                        <Profile image={Tucker} name="Tucker Hehn" role="Coordinator" text="Hi, my name is Tucker Hehn. I'm current a CSU Business major with an Information Systems concentration. I'm currently helping with the research and presentation of SmartEatz. I'm excited to see how we can use develop this site for the betterment of people's diets and all around well-being." />
                    </div>
                </div>
                <div className="dividor-bottom"></div>
                <div className="row">
                    <div>
                        <Profile image={Los} name="Autumn Los" role="Coordinator" text="My name is Autumn Los and I am a senior at Colorado State University pursuing a Major in Business Administration with a dual concentration in Marketing and Computer Information Systems. As a dedicated member of the SmartEatz team, I thrive on the opportunity to make a positive impact in people's lives. Helping individuals reach their goals is a passion of mine that combines my academic knowledge and my dedication to making a difference." />
                    </div>
                    <div>
                        <Profile image={Cole} name="Cole Brewster" role="Coordinator" text="My name is Cole Brewster and I'm a senior at Colorado State University studying Business Administration with a focus in Computer Information Systems. As a member of SmartEatz, I focus on making sure users can achieve any health goal they desire with ease. Together, we're on a mission to make healthy eating a lifestyle that is not only attainable but enjoyable for all. " />
                    </div>
                    <div>
                        <Profile image={Than} name="Than Phihung" role="Coordinator" text="My name is Phihung Than I'm a junior concentrating in CIS. I love being in the outdoors and spending time being active. One one favorite sports playing is basketball. I'm also one of the team members of SmartEatz." />
                    </div>
                </div>
                
            </div>
        </div>
    )
}