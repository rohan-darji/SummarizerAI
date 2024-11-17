from fastapi import FastAPI, File, UploadFile, Form, HTTPException
import fitz  # PyMuPDF
from groq import Groq

# Initialize the Groq client
client = Groq(api_key="")

# FastAPI app instance
app = FastAPI()

# Helper functions
def extract_text_from_pdf(file):
    try:
        doc = fitz.open(stream=file, filetype="pdf")
        text = ""
        for page in doc:
            text += page.get_text()
        return text
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error processing PDF: {e}")


def summarize_text(text: str, summary_type: str):
    prompt_lengths = {
        "short": "Summarize the following text in 50-100 words:",
        "medium": "Summarize the following text in 150-250 words:",
        "long": "Summarize the following text:",
    }
    try:
        summary_response = client.chat.completions.create(
            messages=[
                {"role": "system", "content": "You are a helpful assistant."},
                {"role": "user", "content": f"{prompt_lengths[summary_type]} {text}"},
            ],
            model="llama-3.1-8b-instant",
        )
        return summary_response.choices[0].message.content
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error summarizing text: {e}")


def ask_question(context: str, question: str):
    try:
        answer_response = client.chat.completions.create(
            messages=[
                {"role": "system", "content": "You are a helpful assistant."},
                {"role": "user", "content": f"Context: {context} Question: {question}"},
            ],
            model="llama-3.1-8b-instant",
        )
        return answer_response.choices[0].message.content
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error answering question: {e}")


# API Endpoints
@app.post("/extract-text/")
async def extract_text_endpoint(file: UploadFile = File(...)):
    if file.content_type != "application/pdf":
        raise HTTPException(status_code=400, detail="Invalid file type. Please upload a PDF.")
    content = await file.read()
    text = extract_text_from_pdf(content)
    return {"text": text}


@app.post("/summarize/")
async def summarize_endpoint(
    file: UploadFile = File(...), summary_type: str = Form(...)
):
    if file.content_type != "application/pdf":
        raise HTTPException(status_code=400, detail="Invalid file type. Please upload a PDF.")
    content = await file.read()
    text = extract_text_from_pdf(content)
    summary = summarize_text(text, summary_type)
    return {"summary": summary}


@app.post("/ask-question/")
async def ask_question_endpoint(
    file: UploadFile = File(...), question: str = Form(...)
):
    if file.content_type != "application/pdf":
        raise HTTPException(status_code=400, detail="Invalid file type. Please upload a PDF.")
    content = await file.read()
    text = extract_text_from_pdf(content)
    answer = ask_question(text, question)
    return {"answer": answer}
