'use strict';

var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');



function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public/operation/1', onMessageReceived);


    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = JSON.parse(messageInput.value);
        stompClient.send("/app/1/operation", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(JSON.stringify(message));
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

var socket = new SockJS('/ws');
var stompClient = Stomp.over(socket);

stompClient.connect({}, onConnected, onError);
messageForm.addEventListener('submit', sendMessage, true)