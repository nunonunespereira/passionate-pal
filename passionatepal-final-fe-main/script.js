function textToSpeech(text) {
    //Working with male voice
    let synth = window.speechSynthesis;
    let utterThis = new SpeechSynthesisUtterance(text);
    synth.speak(utterThis);
}


//current selected Conversation id
let conversationId;
//
let selectedConversation;
let loadedConversations = [];
let robotImageUrl = "https://cdn.icon-icons.com/icons2/1371/PNG/512/robot02_90810.png"
let userImageUrl = "https://camo.githubusercontent.com/eb6a385e0a1f0f787d72c0b0e0275bc4516a261b96a749f1cd1aa4cb8736daba/68747470733a2f2f612e736c61636b2d656467652e636f6d2f64663130642f696d672f617661746172732f6176615f303032322d3531322e706e67";
// this will be used to make new conversation window template
let defaultConversation = {
    id : 1,
    messages : [
        {
        role : "assistant",
        content : "Hello! My name is PassionatePal and I'm here to help you with your love and fortune issues. Do go ahead and ask me a question or give me your thoughts and I will try to help with you in the best way!",
        createDateTime : ""
    }
    ]
};

let maxConversations = 13;
//let outerDiv = document.getElementsByClassName("col-12 col-lg-5 col-xl-3 border-right")[0];
let outerDiv = document.getElementById("messages-wrapper");
let overflowId = false;
// track if can send message
let sendButtonActive = true;


// event listeners
document.getElementById("send-button").addEventListener("click", askQuestion, false);
document.getElementById("button-new-conversation").addEventListener("click", createNewConversation, false);

//initialize 
processLoadingConversations();

// This is called to initialize to load the saved conversations to the side bar 
function createConversationElement(conversation)  {

    let aRef = document.createElement('a');

    const lastSeenAt = new Date(conversation.createDateTime[0], conversation.createDateTime[1], conversation.createDateTime[2], conversation.createDateTime[3], conversation.createDateTime[4], conversation.createDateTime[5] );
    let firstUserMessageArray = conversation.messages[1].content.split(" ");
    let parsedFirstUserMessagePreview = '';

    for(let i = 0; i < firstUserMessageArray.length; i++) {
        parsedFirstUserMessagePreview += firstUserMessageArray[i] + " ";
    }

    //let outerDiv = document.getElementsByClassName("col-12 col-lg-5 col-xl-3 border-right")[0];
    outerDiv.appendChild(aRef);

    aRef.outerHTML = '<a href="#" onclick=clickHandler('+conversation.id+') class="list-group-item list-group-item-action border-0">' 
                          +  '<div class="d-flex align-items-start">'
                           +     '<img src="' + robotImageUrl + '" class="rounded-circle mr-1" alt="Passioante Pal" width="40" height="40">'
                           +     '<div class="flex-grow-1 ml-3">'
                           +         'Conversation '  + conversation.id 
                           +         '<div class="small"><span class="fas fa-circle chat-online"></span>' + parsedFirstUserMessagePreview + '</div></div></div></a>'
}

// call to process flow related to calling the generate-answer API and setting the respective data with the result of the async request
// code is very brittle around this part

function askQuestion() {

    // Get prompt value
    var textContent = document.getElementById("send-button-text").value;
    // if waiting for request or if prompt is empty
    if(!sendButtonActive || !textContent) return;


    sendButtonActive = false;


    
    var chatMessages = document.querySelector(".chat-messages");


    

    var newUserChatDiv = document.createElement("div");
    newUserChatDiv.classList.add("chat-message-right");
    newUserChatDiv.classList.add("pb-4");

    // Make an AJAX call to the backend REST API to generate the story
    let url = "http://localhost:8081/passionatepal/api/conversation/generate-answer";

    if(conversationId || conversationId != undefined) {
    // if id wasn't provided then it's a first time conversation and the call doesn't include an id
    url = "http://localhost:8081/passionatepal/api/conversation/generate-answer/?id=" + conversationId;
    }
    else {
        url = "http://localhost:8081/passionatepal/api/conversation/generate-answer"
    }
  
    if(overflowId) url = "http://localhost:8081/passionatepal/api/conversation/generate-answer";
    document.getElementById("send-button-text").value = '';


    
    newUserChatDiv.innerHTML =      '<div style="margin-left: 1rem;"><img src="Resources/carol.png" class="rounded-circle mr-3 " alt="Chris Wood" width="60" height="60">'
                                   +    '<div class="text-muted small text-nowrap mt-2">'+ getNowParsedDate() + '</div></div>'
                                   + '<div class="flex-shrink-1 py-2 px-3 ml-3 mask-custom text-right"><div class="flex-shrink-1 rounded py-2 px-3 ml-3 overflow-y: scroll" style="color: white"><b>You</b></div>' + '<div class="flex-grow-0 py-3 px-4 border-top" style="color: white;">'
                                    +   textContent + '</div>'

    chatMessages.appendChild(newUserChatDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;


    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(textContent);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);


            overflowId = false;
            // Parse the JSON response from the backend REST API
            console.log('xhr.responseText: %s', xhr.responseText);
   

            conversationId = response.id;
            var newChatDiv = document.createElement("div");
            newChatDiv.classList.add("chat-message-left");
            newChatDiv.classList.add("pb-4");
            newChatDiv.innerHTML = '<div><img src="Resources/ele.png" class="rounded-circle mr-1" alt="You" width="60" height="60">'
            +    '<div class="text-muted small text-nowrap mt-2">'+ getNowParsedDate() + '</div></div>'
            + '<div class="flex-shrink-1 py-2 px-3 ml-3 mask-custom text-left"><div class="flex-shrink-1 rounded py-2 px-3 ml-3 overflow-y: scroll" style="color: white; overflow-y: scroll"><b>PassionatePal</b></div>' + '<div class="flex-grow-0 py-3 px-4 border-top" style="color: white; overflow-y: scroll">'
             +   response.content + '</div>'

             



            clearConversations();    
            processLoadingConversations();
            chatMessages.appendChild(newChatDiv);
            textToSpeech(response.content);

        }
        // send button will be deactivated until we have response from the server
        sendButtonActive = true;
        

        }

}

// this creates the html related to a message
function createMessage(isUser, message, date) {


    let img = new Image();
    img.src = isUser ?  'carol.png' : 'ele.png';
    let imageUrl = isUser ?  'carol.png' : 'ele.png';
    let userName = isUser ?  "You" : "PassionatePal";
    let chatMessageDivElement = isUser ? '<div class="chat-message-right pb-4">' : '<div class="card mask-custom">';
    return chatMessageDivElement + '<div><img src="Resources/ele.png" class="rounded-circle mr-1"< alt="You" width="40" height="40">'
    +  '<div class="text-muted small text-nowrap mt-2">'+ getParsedDate(date) + '</div></div>'
    +  '<div><div class="card mask-custom">' + '<div class="card mask-custom" style="color: white;"><div class="font-weight-bold mb-1">' + userName + '</div>'
     +   message.content + '</div></div></div></div>';
}



// call back function click handler
function loadClickConversation(id) {

    // clear messages
    conversationId = id;
    let conversation = loadedConversations.filter(v => v.id === id)[0];
    // retrieve messages container
    let chatMessages = document.getElementsByClassName("chat-messages p-4")[0];
    chatMessages.innerHTML = '';
    
    for(let i = 0; i < conversation.messages.length; i++) {
        let message = conversation.messages[i];
        if(message.role === 'system') continue;
        let isUser = message.role === 'user' ? true : false; 
        let divToAdd= document.createElement("div");
        chatMessages.appendChild(divToAdd);
        divToAdd.outerHTML = createMessage(isUser, message, message.createDateTime);
    }
}

// converts now-time hour to display format
function getNowParsedDate() {

    const event = new Date();
    let messageTime = event.toLocaleTimeString('en-US');
    let splitMessageTimeString = messageTime.split(":");
    let parsedMessageTime = splitMessageTimeString[0] + ":" + splitMessageTimeString[1] + " " +  messageTime.split(" ")[1].toLowerCase();

    return parsedMessageTime;
}

// converts passed date in array format to display format
function getParsedDate(date) {

    const event = new Date(date[0], date[1], date[2], date[3], date[4]);
    let messageTime = event.toLocaleTimeString('en-US');
    let splitMessageTimeString = messageTime.split(":");
    let parsedMessageTime = splitMessageTimeString[0] + ":" + splitMessageTimeString[1] + " " +  messageTime.split(" ")[1].toLowerCase();

    return parsedMessageTime;
}

// Listener click callback 
function clickHandler(id) {
    loadClickConversation(id);

}

// this is called when clicking the Add new conversation button
function createNewConversation() {

    // Don't allow to spam the add new conversation button
    overflowId = true;

    // only 1 new conversation tab
    if(hasNewConversationOpened) return;
    hasNewConversationOpened = true;
    let chatMessages = document.getElementsByClassName("chat-messages p-4")[0];
    chatMessages.innerHTML = '';

    let message = defaultConversation.messages[0];

    let divToAdd= document.createElement("div");
    chatMessages.prepend(divToAdd);
    let now = new Date();
    let dateArray = [now.getFullYear(), now.getMonth(), now.getDay(), now.getHours(), now.getMinutes(), now.getSeconds()];
    defaultConversation.messages[0].createDateTime = dateArray;
    divToAdd.outerHTML = createMessage(false, message, dateArray);
    defaultConversation.id = loadedConversations[loadedConversations.length - 1].id + 1;
    loadedConversations.push(defaultConversation);
    conversationId = null;

    let aRef = document.createElement('a');

    let outerDiv = document.getElementById("messages-wrapper");
    outerDiv.prepend(aRef);

    aRef.outerHTML = '<a href="#" onclick=clickHandler('+ defaultConversation.id +') class="list-group-item list-group-item-action border-0">' 
                              +  '<div class="d-flex align-items-start">'
                               +     '<img src="Resources/ele.png" class="rounded-circle mr-1" alt="PassionatePal" width="40" height="40">'
                               +     '<div class="flex-grow-1 ml-3">'
                               +         'Conversation '  + defaultConversation.id 
                               +         '<div class="small"><span class="fas fa-circle chat-online"></span>' + "Initiate Conversation" + '</div></div></div></a>'
    }


    // calls to get all conversations API and sets side bar to conversations
    function processLoadingConversations() {
        overflowId = false;
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "http://localhost:8081/passionatepal/api/conversation/get-all-conversations", true);
        xhr.send();
    
        selectedConversation = document.getElementsByClassName(".d-flex .align-items-start");
    
    
        
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                hasNewConversationOpened = false;
                console.log('xhr.responseText: %s', xhr.responseText);
                var conversations = JSON.parse(xhr.responseText);
                loadedConversations = conversations;
                let outerDiv = document.getElementById("messages-wrapper");
    
                if(conversations.length > 0) {
                   outerDiv.innerHTML = '';
                }
    
                for(let i = conversations.length - 1; i >= conversations.length - maxConversations; i--) {
                    createConversationElement(conversations[i]);
                }
                
                if(conversations.length > 0) {
                    selectedConversation = conversations.length - 1;
                   // loadClickConversation(conversations.length);
                } else {
                    createConversationElement(defaultConversation);
                    hasNewConversationOpened = true;
                } 
            }
        };
    }



// clears conversations
    function clearConversations() {
        let wrapperElement = document.getElementById("messages-wrapper");
        wrapperElement.innerHTML = '';
    }

    var lastAudio = null;
      function playMusic(id) {
        var audio = document.getElementById(id);
        if (lastAudio && lastAudio!== audio) {
            lastAudio.pause();
            lastAudio.currentTime = 0;
        }
        audio.play();
        lastAudio = audio;
    }
