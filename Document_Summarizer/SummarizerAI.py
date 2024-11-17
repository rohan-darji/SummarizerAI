import streamlit as st
import fitz  # PyMuPDF
from groq import Groq
from PIL import Image

# Initialize the Groq client
client = Groq(api_key='')


def extract_text_from_pdf(pdf_path):
    doc = fitz.open(pdf_path)
    text = ""
    for page in doc:
        text += page.get_text()
    return text


def summarize_text(text, summary_type):
    prompt_lengths = {
        "short": "Summarize the following text in 50-100 words:",
        "medium": "Summarize the following text in 150-250 words:",
        "long": "Summarize the following text:"
    }
    try:
        summary_response = client.chat.completions.create(
            messages=[
                {"role": "system", "content": "You are a helpful assistant."},
                {"role": "user", "content": f"{prompt_lengths[summary_type]} {text}"}
            ],
            model="llama-3.1-8b-instant",
        )
        return summary_response.choices[0].message.content
    except Exception as e:
        return f"An error occurred: {e}"


def ask_question(context, question):
    try:
        answer_response = client.chat.completions.create(
            messages=[
                {"role": "system", "content": "You are a helpful assistant."},
                {"role": "user", "content": f"Context: {context} Question: {question}"}
            ],
            model="llama-3.1-8b-instant",
        )
        return answer_response.choices[0].message.content
    except Exception as e:
        return f"An error occurred: {e}"


# Streamlit UI

col1, col2 = st.columns([1, 4])

with col1:
    image = Image.open('SummarizerAI.png')
    st.image(image, use_container_width=True)

with col2:
    st.markdown(
        """
        <div style='
            background-color: white; 
            border-radius: 10px; 
            padding: 10px; 
            margin-top: 10px; 
            text-align: center;'>
            <h1 style='font-family: "Inter"; color: black;'>
                Summarizer AI
            </h1>
        </div>
        """,
        unsafe_allow_html=True
    )

st.markdown(
    """
    <h6 style='font-family: Inter; color: black; padding: 0;'>
    Upload your PDF for AI Summarization and Q&A 📄
    </h6>
    """,
    unsafe_allow_html=True
)
uploaded_file = st.file_uploader("", type="pdf")

if uploaded_file is not None:
    pdf_text = extract_text_from_pdf(uploaded_file)

    st.markdown(
        """
        <div style='
            background-color: white; 
            border-radius: 10px; 
            margin-top: 10px; 
            '>
            <h6 style='font-family: "Inter"; color: black;'>
                Gist of the PDF:
            </h6>
        </div>
        """,
        unsafe_allow_html=True
    )
    st.write(pdf_text[:500])  # Display a snippet of the text for review

    if st.button("Short Summary"):
        summary = summarize_text(pdf_text, "short")
        st.subheader("Summary:")
        st.write(summary)

    if st.button("Medium Summary"):
        summary = summarize_text(pdf_text, "medium")
        st.subheader("Summary:")
        st.write(summary)

    if st.button("Long Summary"):
        summary = summarize_text(pdf_text, "long")
        st.subheader("Summary:")
        st.write(summary)

    st.markdown(
    """
    <h6 style='font-family: Inter; color: black; padding: 0;'>
    Ask a question about the PDF:
    </h6>
    """,
    unsafe_allow_html=True
    )
    question = st.text_input("")
    if question:
        answer = ask_question(pdf_text, question)
        st.subheader("Answer:")
        st.write(answer)
